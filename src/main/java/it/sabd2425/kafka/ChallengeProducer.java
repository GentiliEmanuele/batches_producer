package it.sabd2425.kafka;

import it.sabd2425.kafka.config.Configuration;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ChallengeProducer {
    private final static String PRODUCER_ID = "gca-prod";
    private String topic;
    private Producer<Integer, byte[]> producer;

    public ChallengeProducer(String topic) {
        this.topic = topic;
        producer = createProducer();
    }

    private static Producer<Integer, byte[]> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Configuration.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public void produce(Integer key, byte[] value) {
        try {
            final var record = new ProducerRecord<>(topic, key, value);
            RecordMetadata metadata = producer.send(record).get();
            System.out.printf("sent record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                    record.key(), record.value(), metadata.partition(), metadata.offset());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        producer.flush();
        producer.close();
    }
}
