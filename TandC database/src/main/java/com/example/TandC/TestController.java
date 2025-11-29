package com.example.TandC;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to demonstrate Optimistic Locking with JPA @Version annotation.
 * This test simulates two concurrent threads trying to update the same wallet.
 */
@RestController
public class TestController {

    private final WalletService service;

    public TestController(WalletService service) {
        this.service = service;
    }

    /**
     * Test endpoint to demonstrate optimistic locking behavior.
     * 
     * Scenario:
     * - Two threads (T1 and T2) start simultaneously
     * - Both read the same wallet with version 0
     * - Both try to update the balance
     * - T1 commits first → version becomes 1
     * - T2 tries to commit → OptimisticLockException (version mismatch)
     * - T2 retries and eventually succeeds
     * 
     * @return Message to check console logs
     */
    @GetMapping("/test-lock")
    public String test() throws Exception {

        // Thread 1: Simple transaction without retry logic
        Thread t1 = new Thread(() -> {
            try {
                service.addMoney(1L, 100);
            } catch (Exception e) {
                System.out.println("T1 FAILED → " + e.getClass().getSimpleName());
            }
        }, "T1");

        // Thread 2: Transaction with retry mechanism to handle OptimisticLockException
        Thread t2 = new Thread(() -> {
            int maxRetries = 3; // Maximum number of retry attempts
            int attempt = 0;
            boolean success = false;

            // Retry loop: Keep trying until success or max retries reached
            while (attempt < maxRetries && !success) {
                attempt++;
                try {
                    System.out.println("T2 Attempt #" + attempt);
                    service.addMoney(1L, 100);
                    success = true; // Mark as successful
                    System.out.println("T2 SUCCESS on attempt #" + attempt);
                } catch (Exception e) {
                    // OptimisticLockException occurs when version mismatch is detected
                    System.out.println("T2 Attempt #" + attempt + " FAILED → " + e.getClass().getSimpleName());

                    if (attempt < maxRetries) {
                        System.out.println("T2 retrying...");
                        try {
                            // Small delay before retry to allow T1 to complete
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        System.out.println("T2 FAILED after " + maxRetries + " attempts");
                    }
                }
            }
        }, "T2");

        // Start both threads simultaneously to create race condition
        t1.start();
        t2.start();

        // Wait for both threads to complete
        t1.join();
        t2.join();

        return "Check logs for result";
    }
}
