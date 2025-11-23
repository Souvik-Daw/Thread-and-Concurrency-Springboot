import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private int balance = 100;
    private final ReentrantLock lock = new ReentrantLock(true); // fairness true

    public void withdraw(String threadName) {
        lock.lock(); // acquire
        try {
            System.out.println(threadName + " got lock");
            if (balance >= 10) {
                balance -= 10;
                System.out.println(threadName + " withdrew 10, balance=" + balance);
            } else {
                System.out.println(threadName + " insufficient balance");
            }
        } finally {
            lock.unlock(); // always unlock
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample bank = new ReentrantLockExample();
        Runnable task = () -> bank.withdraw(Thread.currentThread().getName());

        for (int i = 0; i < 5; i++) {
            new Thread(task).start();
        }

        System.out.println("Final balance: " + bank.balance);
    }
}
