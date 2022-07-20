package os.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author leo
 * @date 20220708 02:19:35
 */
public class CompareMap {
    private static Map<Short,Double> cmap = new ConcurrentHashMap<>();
    private static Map<Short,Double> smap = Collections.synchronizedMap(new HashMap<>());
    public static void cMultiInsert() {
        final int threads = 100000;
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        Instant begin = Instant.now();
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                cmap.put((short)Math.random(),Math.random());
                for (int j = 0; j < 10; j++) {
                    cmap.get((short)Math.random());
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
            long l = Duration.between(begin, Instant.now()).toMillis();
            System.out.println("cmap spend "+ l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void sMultiInsert() {
        final int threads = 100000;
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        Instant begin = Instant.now();
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                smap.put((short)Math.random(),Math.random());
                for (int j = 0; j < 10; j++) {
                    smap.get((short)Math.random());
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
            long l = Duration.between(begin, Instant.now()).toMillis();
            System.out.println("smap spend "+ l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        cMultiInsert();
        sMultiInsert();
    }
}
