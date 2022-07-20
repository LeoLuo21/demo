package os.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leo
 * @date 20220708 01:41:30
 */
public class SegmentCounter implements Counter{
    private static final int segments = Runtime.getRuntime().availableProcessors();
    private static final Object[] locks = new Object[segments];
    private static final AtomicInteger index = new AtomicInteger();
    static {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }
    private volatile long globalCount;

    private long[] localCount = new long[segments];

    public SegmentCounter(long count) {
        this.globalCount = count;
        long local = globalCount / segments;
        long mod = globalCount % segments;
        for (int i = 0; i < segments; i++) {
            this.localCount[i] = local;
        }
        for(int i = 0; i < mod; i++) {
            this.localCount[i]++;
        }
    }
    @Override
    public long getCount() {
        return globalCount;
    }

    @Override
    public void minusCount() {
        if (globalCount <= 0) return;
        int i = index.getAndIncrement() % segments;
        while (true) {
            synchronized (locks[i]) {
                if (localCount[i] > 0) {
                    localCount[i]--;
                    synchronized (this) {
                        globalCount -= 1;
                    }
                    break;
                }else {
                    if (globalCount <= 0) break;
                    i = (i + 1) % segments;
                }
            }
        }
    }

    public static void main(String[] args) {
        multi();
    }
    public static void multi() {
        final int concurrency = 10000000;
        Counter counter = new SegmentCounter(10000);
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
