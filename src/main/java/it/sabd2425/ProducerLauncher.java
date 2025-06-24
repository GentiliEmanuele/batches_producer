package it.sabd2425;


import it.sabd2425.cli.StartBenchmarkCommand;
import it.sabd2425.gc25client.RestApiClient;
import it.sabd2425.gc25client.errors.DefaultApiException;
import it.sabd2425.kafka.ChallengeProducer;
import it.sabd2425.kafka.KafkaProducerFactory;
import it.sabd2425.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerLauncher {

    private static final Logger LOGGER = Logger.getLogger(ProducerLauncher.class.getName());

    public static void main(String[] args) throws DefaultApiException, InterruptedException {
        var benchConfig = Utils.toBenchConfig(StartBenchmarkCommand.fromArgs(args));
        var endpoint = Utils.getOrThrow("GC25_API_ENDPOINT");
        var client = new RestApiClient(endpoint);
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
                LOGGER.log(Level.INFO, String.format("retrieved successfully %d batches", batchCount));
                producer.publishBatch(benchmark, o.get());
                if (batchCount % batchSize == 0) {
                    producer.flush();
                }
            }
        }
    }

    private static ChallengeProducer createProducer() {
        return new KafkaProducerFactory().create();
    }
}
