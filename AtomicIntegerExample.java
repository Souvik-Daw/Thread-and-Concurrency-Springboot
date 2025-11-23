import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
 
    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            for(int i=0;i<10000;i++)
                count.incrementAndGet(); // CAS internally
        });
        Thread t2 = new Thread(() -> {
            for(int i=0;i<10000;i++)
                count.incrementAndGet();
        });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Count = " + count.get()); // ALWAYS 20000
    }
}
