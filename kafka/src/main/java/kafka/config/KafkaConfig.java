package kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.*;
import java.util.Properties;

/**
 * @author leo
 * @date 20220628 00:49:27
 */
public class KafkaConfig {
    public static KafkaProducer getKafkaProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-1");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"120.48.40.214:9093");
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return new KafkaProducer(config);
    }

    public static class GeneralSerializer implements Serializer<Object> {

        @Override
        public byte[] serialize(String s, Object o) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bo);
                oos.writeObject(o);
                return bo.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GeneralDeserializer implements Deserializer<Object> {

        @Override
        public Object deserialize(String s, byte[] bytes) {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = null;
            Object message = null;
            try {
                ois = new ObjectInputStream(bi);
                message =  ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return message;
        }
    }
}
