package it.sabd2425;

import it.sabd2425.cli.Command;
import it.sabd2425.gc25client.RestApiClient;
import it.sabd2425.gc25client.data.BenchConfig;
import it.sabd2425.gc25client.errors.DefaultApiException;
import it.sabd2425.kafka.ChallengeProducer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerLauncher {
    private static final Logger LOGGER = Logger.getLogger(ProducerLauncher.class.getName());
    private final static String TOPIC = "batches";
    public static void main(String[] args) throws DefaultApiException {
        var benchConfig = parse(args);
        ChallengeProducer producer = new ChallengeProducer(TOPIC);
        var client = new RestApiClient("http://localhost:8866/api");
        var benchmark = client.create(benchConfig);
        client.start(benchmark);
        var limit = benchConfig.getMaxBatches();
        var batchCount = 0;
        while (limit--  > 0) {
            var o = client.nextBatch(benchmark);
            if (o.isEmpty()) {
                LOGGER.log(Level.INFO, () -> String.format("retrieved successfully %d/%d batches", batchCount, benchConfig.getMaxBatches()));
            }
            ++batchCount;
            // TODO: send batch to KAFKA
            // producer.produce(, o.get());
        }
        client.end(benchmark);
    }

    private static BenchConfig parse(String[] args) {
        var command = new Command();
        new picocli.CommandLine(command).parseArgs(args);
        return new BenchConfig(command.getApiToken(), command.getName(), command.getLimit(), command.isTest());
    }
}
