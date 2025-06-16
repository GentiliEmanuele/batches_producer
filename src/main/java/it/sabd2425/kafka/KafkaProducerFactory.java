package it.sabd2425.kafka;

import java.util.Properties;

public class KafkaProducerFactory {
    public ChallengeProducer create() {
        return new ChallengeProducer(getOrThrow("KAFKA_TOPIC"), getKafkaConfiguration());
    }

    private Properties getKafkaConfiguration() {
        var config = new Properties();
        set(config, "client.id", "KAFKA_CLIENT_ID");
        set(config, "bootstrap.servers", "KAFKA_BOOTSTRAP_SERVERS");
        set(config, "acks", "KAFKA_ACKS");
        config.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return config;
    }

    private void set(Properties config, String propertyName, String envName) {
        config.put(propertyName, getOrThrow(envName));
    }

    private String getOrThrow(String name) {
        var val = System.getenv(name);
        if (val == null) {
            throw new IllegalStateException(name + " environment variable is not set");
        }
        return val;
    }
}
