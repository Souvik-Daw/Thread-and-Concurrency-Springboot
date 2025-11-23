import java.util.concurrent.*;

public class MultiServiceExample {

    // Fake service A
    static String serviceA() throws InterruptedException {
        Thread.sleep(500);
        return "Service A response";
    }

    // Fake service B
    static String serviceB() throws InterruptedException {
        Thread.sleep(700);
        return "Service B response";
    }

    // Fake service C
    static String serviceC() throws InterruptedException {
        Thread.sleep(300);
        return "Service C response";
    }

    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        // Submit 3 tasks in parallel
        Future<String> f1 = pool.submit(() -> {
            System.out.println("Calling Service A on: " + Thread.currentThread().getName());
            return serviceA();
        });

        Future<String> f2 = pool.submit(() -> {
            System.out.println("Calling Service B on: " + Thread.currentThread().getName());
            return serviceB();
        });

        Future<String> f3 = pool.submit(() -> {
            System.out.println("Calling Service C on: " + Thread.currentThread().getName());
            return serviceC();
        });

        // Gather results (waits for each to finish)
        String r1 = f1.get();
        String r2 = f2.get();
        String r3 = f3.get();

        System.out.println("----- RESULTS -----");
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);

        pool.shutdown();
    }
}
