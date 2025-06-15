package it.sabd2425.kafka;

import it.sabd2425.gc25client.RestApiClient;
import it.sabd2425.gc25client.data.BenchConfig;
import it.sabd2425.gc25client.data.Centroid;
import it.sabd2425.gc25client.data.QueryResult;
import it.sabd2425.gc25client.errors.DefaultApiException;

import java.util.List;

public class ProducerLauncher {
    private final static String TOPIC = "batches";
    public static void main(String[] args) throws DefaultApiException {
        ChallengeProducer producer = new ChallengeProducer(TOPIC);
        var client = new RestApiClient("http://localhost:8866/api");
        var limit = 3;
        var benchmark = client.create(new BenchConfig("sandro", "autoban", limit, true));
        client.start(benchmark);
        while (limit --  > 0) {
            var o = client.nextBatch(benchmark);
            var batch = o.get();
            System.out.println(batch);
            producer.produce(batch.getBatchId(), batch);
            var timestamp = client.postResult(benchmark, new QueryResult(batch.getBatchId(), 0, batch.getPrintId(), batch.getTileId(), 11, List.of(new Centroid(1, 2, 3))));
            System.out.println(timestamp);
        }
        client.end(benchmark);
    }
}
