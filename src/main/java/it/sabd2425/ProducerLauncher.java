package it.sabd2425;

import it.sabd2425.cli.Command;
import it.sabd2425.gc25client.RestApiClient;
import it.sabd2425.gc25client.data.BenchConfig;
import it.sabd2425.gc25client.errors.DefaultApiException;
import it.sabd2425.kafka.ChallengeProducer;
import it.sabd2425.kafka.KafkaProducerFactory;

public class ProducerLauncher {
    public static void main(String[] args) throws DefaultApiException {
        var benchConfig = parse(args);
        var client = createClient();
        var batchSize = 64;
        try (var producer = createProducer()) {
            var benchmark = client.create(benchConfig);
            client.start(benchmark);
            var batchCount = 0;
            while (true) {
                var o = client.nextBatch(benchmark);
                if (o.isEmpty()) {
                    break;
                }
                ++batchCount;
                producer.publishBatch(benchmark, o.get());
                if (batchCount % batchSize == 0) {
                    producer.flush();
                }
            }
        }
    }

    private static BenchConfig parse(String[] args) {
        var command = new Command();
        new picocli.CommandLine(command).parseArgs(args);
        var limit = command.getLimit();
        return limit
                .map(integer -> new BenchConfig(command.getApiToken(), command.getName(), integer, command.isTest()))
                .orElseGet(() -> new BenchConfig(command.getApiToken(), command.getName(), command.isTest()));
    }

    private static RestApiClient createClient() {
        var endpoint = System.getenv("GC25_API_ENDPOINT");
        if (endpoint == null) {
            throw new IllegalArgumentException("Environment variable GC25_API_ENDPOINT not set");
        }
        return new RestApiClient(endpoint);
    }

    private static ChallengeProducer createProducer() {
        return new KafkaProducerFactory().create();
    }
}
