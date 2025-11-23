import java.util.concurrent.*;

public class ThreadNameSessionExample {

    private static ThreadLocal<String> sessionId = ThreadLocal.withInitial(() -> 
        "SID-" + java.util.UUID.randomUUID()
    );

    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            String sid = sessionId.get();
            System.out.println("Thread: " + threadName + " | Session ID: " + sid);
        };

        // submit multiple tasks
        for (int i = 0; i < 6; i++) {
            pool.submit(task);
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
