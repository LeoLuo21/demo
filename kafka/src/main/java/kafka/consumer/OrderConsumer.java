package kafka.consumer;

import kafka.config.KafkaConfig;
import kafka.message.OrderMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leo
 * @date 20220627 22:19:27
 */
public class OrderConsumer {
    private final KafkaConsumer<String,OrderMessage> kafkaConsumer;

    private final AtomicInteger count = new AtomicInteger();

    private final Executor executor = new ThreadPoolExecutor(10,20,5, TimeUnit.MINUTES,new LinkedBlockingQueue<>());

    public OrderConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.CLIENT_ID_CONFIG,"order-consumer-1");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"120.48.40.214:9093");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"order-consumer-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConfig.GeneralDeserializer.class.getName());
        this.kafkaConsumer = new KafkaConsumer<>(config);
    }

    public void processOrderMessage() {
        executor.execute(() -> {
            kafkaConsumer.subscribe(Collections.singleton("order-topic"));
            while (true) {
                ConsumerRecords<String, OrderMessage> records = kafkaConsumer.poll(Duration.ofSeconds(10));
                records.forEach( i -> {
                    OrderMessage orderMessage = (OrderMessage) i.value();
                    System.out.println(orderMessage);
                });
                kafkaConsumer.commitAsync();
            }
        });
    }

}
