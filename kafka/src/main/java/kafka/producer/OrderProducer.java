package kafka.producer;

import kafka.config.KafkaConfig;
import kafka.message.OrderMessage;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leo
 * @date 20220627 21:51:35
 */
public class OrderProducer {

    private final AtomicInteger count = new AtomicInteger();
    private final KafkaProducer<String, OrderMessage> kafkaProducer;

    public OrderProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "order-producer-1");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"120.48.40.214:9093");
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.GeneralSerializer.class.getName());
        this.kafkaProducer = new KafkaProducer<>(config);
    }

    public void sendMessage(OrderMessage orderMessage) {
        ProducerRecord<String,OrderMessage> record = new ProducerRecord<>("order-topic",orderMessage);
        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    System.out.println("Exception: "+e.getMessage());
                }
            }
        });
    }
}
