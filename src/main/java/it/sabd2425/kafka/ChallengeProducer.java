package it.sabd2425.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ChallengeProducer implements AutoCloseable {
    private final String topic;
    private final KafkaProducer<String, byte[]> producer;

    public ChallengeProducer(String topic, Properties kafkaConfig) {
        this.topic = topic;
        producer = new KafkaProducer<>(kafkaConfig);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close();
    }

    public void publishBatch(String key, byte[] batch) {
        producer.send(new ProducerRecord<>(topic, key, batch));
    }

    public void flush() {
        producer.flush();
    }
}
