import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) throws Exception {

        // Create a thread pool with 4 threads
        ExecutorService pool = Executors.newFixedThreadPool(4);

        // 1️⃣ Submit a Runnable (no return result)
        Runnable runnableTask = () -> {
            System.out.println("Runnable executed by: " + Thread.currentThread().getName());
        };

        pool.submit(runnableTask); // Will print from some thread

        // 2️⃣ Submit a Callable (returns a value)
        Callable<String> callableTask = () -> {
            Thread.sleep(500); // simulate work
            return "Callable result from: " + Thread.currentThread().getName();
        };

        Future<String> future = pool.submit(callableTask);

        // Wait for result
        String result = future.get();  // blocks until callable is done
        System.out.println(result);

        // Shutdown pool
        pool.shutdown();
    }
}
