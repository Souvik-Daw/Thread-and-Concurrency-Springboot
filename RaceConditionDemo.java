import java.util.concurrent.*;

public class RaceConditionDemo {

    private int unsafeCounter = 0;     // will cause race condition
    private int safeCounter = 0;       // fixed using lock

    public synchronized void incrementSafe() {
        safeCounter++;
    }

    public void incrementUnsafe() {
        unsafeCounter++;  // NOT synchronized (race condition)
    }

    public static void main(String[] args) throws Exception {

        RaceConditionDemo obj = new RaceConditionDemo();
        ExecutorService pool = Executors.newFixedThreadPool(5);

        Runnable unsafeTask = () -> {
            for (int i = 0; i < 10000; i++) {
                obj.incrementUnsafe();
            }
        };

        Runnable safeTask = () -> {
            for (int i = 0; i < 10000; i++) {
                obj.incrementSafe();
            }
        };

        // Run both 5 times -> total 50,000 increments each
        for (int i = 0; i < 5; i++) pool.submit(unsafeTask);
        for (int i = 0; i < 5; i++) pool.submit(safeTask);

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Final UNSAFE Counter = " + obj.unsafeCounter);
        System.out.println("Final SAFE Counter   = " + obj.safeCounter);
    }
}
