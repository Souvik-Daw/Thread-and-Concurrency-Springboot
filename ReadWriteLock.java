import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {
  private String product = "Laptop";
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String read() {
        rwLock.readLock().lock(); //many readers allowed
        try {
            System.out.println(Thread.currentThread().getName() + " reading: " + product);
            return product;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void update(String newProduct) {
        rwLock.writeLock().lock(); //only one writer allowed
        try {
            System.out.println(Thread.currentThread().getName() + " updating...");
            product = newProduct;
            System.out.println("Updated to " + product);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock pc = new ReadWriteLock();

        Runnable reader = () -> pc.read();
        Runnable writer = () -> pc.update("Mobile");

        // 3 readers
        for (int i = 0; i < 3; i++) new Thread(reader).start();

        Thread.sleep(100); // ensure readers start first

        new Thread(writer).start(); // writer
        Thread.sleep(100); // ensure writer starts
        new Thread(reader).start(); // another reader
    }
}