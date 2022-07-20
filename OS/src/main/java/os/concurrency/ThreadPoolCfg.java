package os.concurrency;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author leo
 * @date 20220708 04:05:52
 */
public class ThreadPoolCfg {

    private static final int processors = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(processors,processors,0,TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }
    public static void execute(Runnable r) {
        executor.execute(r);
    }
}
