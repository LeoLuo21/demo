package kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.consumer.LogConsumer;
import kafka.consumer.OrderConsumer;
import kafka.message.LogMessage;
import kafka.message.OrderMessage;
import kafka.producer.LogProducer;
import kafka.producer.OrderProducer;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * @author leo
 * @date 20220627 22:51:13
 */
public class Main {
    public static void main(String[] args) {

    }

    public static void testSerialization() {
        try {
            final int length = 10000;
            ObjectMapper om = new ObjectMapper();
            Instant begin = Instant.now();
            for (int i = 0; i < length; i++) {
                OrderMessage orderMessage = new OrderMessage();
                orderMessage.setCustomer("leo");
                orderMessage.setOrderNumber(UUID.randomUUID().toString());
                byte[] bytes = om.writeValueAsBytes(orderMessage);
                OrderMessage value = om.readValue(bytes, OrderMessage.class);
                //System.out.println("jackson bytes length "+bytes.length);
            }
            Duration spend = Duration.between(begin, Instant.now());
            System.out.println("jackson spend "+ spend.toMillis());
            begin = Instant.now();
            for (int i = 0; i < length; i++) {
                OrderMessage orderMessage = new OrderMessage();
                orderMessage.setCustomer("leo");
                orderMessage.setOrderNumber(UUID.randomUUID().toString());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(orderMessage);
                byte[] bytes = baos.toByteArray();
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                OrderMessage value = (OrderMessage) ois.readObject();
                //System.out.println("jdk bytes length "+bytes.length);
            }
            spend = Duration.between(begin, Instant.now());
            System.out.println("jdk spend "+ spend.toMillis());
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void testKafka() {
        LogProducer logProducer = new LogProducer();
        LogMessage logMessage = new LogMessage();
        logMessage.setMessage("This is a log");
        logProducer.sendLog(logMessage);
        OrderProducer orderProducer = new OrderProducer();
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderNumber("order-1");
        orderMessage.setCustomer("leo");
        orderProducer.sendMessage(orderMessage);
        LogConsumer logConsumer = new LogConsumer();
        logConsumer.processLogMessage();
        OrderConsumer orderConsumer = new OrderConsumer();
        orderConsumer.processOrderMessage();
    }

}
