import java.util.concurrent.*;

public class synchronized_volatile_ThreadLocal {

    public int counter = 0;              // regular counter
    private int syncCounter = 0;           // used with synchronized
    private volatile boolean running = true; // used with volatile
    private ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 100);

    public synchronized void incrementSync() {
        syncCounter++;
    }

    public static void main(String[] args) throws Exception {

        synchronized_volatile_ThreadLocal obj = new synchronized_volatile_ThreadLocal();

        // Executor with 3 threads
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // 1️⃣ synchronized example (safe increment)
        Runnable syncTask = () -> {
            for (int i = 0; i < 10000; i++) {
                obj.incrementSync();
            }
        };
        pool.submit(syncTask);
        pool.submit(syncTask);
        pool.submit(syncTask);


        Runnable unsyncTask = () -> {
            for (int i = 0; i < 10000; i++) {
                obj.counter++; // not thread-safe
            }
        };
        pool.submit(unsyncTask);
        pool.submit(unsyncTask);
        pool.submit(unsyncTask);

        //2️⃣ volatile example (stop signal)
        Runnable volatileTask = () -> {
            while (obj.running) {
                // busy work
                //System.out.println("Inside loop...");
            }
            System.out.println("Volatile flag received, thread stopped.");
        };

        Future<?> f1 = pool.submit(volatileTask);

        Thread.sleep(500);
        obj.running = false; // stop signal visible immediately

        f1.get(); // wait for completion of this thread

        // 3️⃣ ThreadLocal example
        Runnable localTask = () -> {
            System.out.println("ThreadLocal initial: " + obj.local.get());
            obj.local.set(obj.local.get() + 1);
            System.out.println("ThreadLocal updated: " + obj.local.get());
        };

        pool.submit(localTask);
        pool.submit(localTask);
        pool.submit(localTask);

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("Final synchronized counter = " + obj.syncCounter);
        System.out.println("Final unsynchronized counter = " + obj.counter);
    }
}
