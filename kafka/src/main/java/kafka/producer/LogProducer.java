package kafka.producer;

import kafka.config.KafkaConfig;
import kafka.message.LogMessage;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author leo
 * @date 20220701 18:09:57
 */
public class LogProducer {

    private KafkaProducer<String, LogMessage> kafkaProducer;
    public LogProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "log-producer-1");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"120.48.40.214:9093");
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.GeneralSerializer.class.getName());
        this.kafkaProducer = new KafkaProducer(config);
    }

    public void sendLog(LogMessage logMessage) {
        ProducerRecord<String, LogMessage> record = new ProducerRecord<String, LogMessage>("log-topic", logMessage);
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
