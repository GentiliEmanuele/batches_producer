package it.sabd2425.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.sabd2425.gc25client.data.Batch;
import org.apache.kafka.common.serialization.Serializer;

public class BatchSerializer implements Serializer<Batch> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String s, Batch batch) {
        try {
            return objectMapper.writeValueAsBytes(batch);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
