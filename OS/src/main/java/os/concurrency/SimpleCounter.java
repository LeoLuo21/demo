package os.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

/**
 * @author leo
 * @date 20220708 01:33:40
 */
public class SimpleCounter implements Counter{
    private volatile long count;

    public SimpleCounter(long count) {
        this.count = count;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public synchronized void minusCount() {
        if (count > 0) {
            count--;
        }
    }

    public static void main(String[] args) {
        multi();
    }
    public static void multi() {
        final int concurrency = 10000000;
        Counter counter = new SimpleCounter(10000);
        final CountDownLatch countDownLatch = new CountDownLatch(concurrency);
        Instant begin = Instant.now();
        for (int i = 0; i < concurrency; i++) {
            ThreadPoolCfg.execute(() -> {
                counter.minusCount();
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            long l = Duration.between(begin, Instant.now()).toMillis();
            System.out.println("multi spend " + l + " millis");
            System.out.println("counter = " + counter.getCount());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
