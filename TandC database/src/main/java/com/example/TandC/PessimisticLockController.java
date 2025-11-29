package com.example.TandC;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessimisticLockController {

    private final WalletService service;

    public PessimisticLockController(WalletService service) {
        this.service = service;
    }

    @GetMapping("/test-pessimistic")
    public String testPessimistic() throws Exception {
        // Thread 1
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("T1 Starting...");
                service.addMoneyWithPessimisticLock(1L, 100);
                System.out.println("T1 Finished");
            } catch (Exception e) {
                System.out.println("T1 FAILED: " + e.getMessage());
            }
        }, "T1");

        // Thread 2
        Thread t2 = new Thread(() -> {
            try {
                // Sleep briefly to ensure T1 starts first and acquires the lock
                // Thread.sleep(100);
                System.out.println("T2 Starting ");
                service.addMoneyWithPessimisticLock(1L, 100);
                System.out.println("T2 Finished");
            } catch (Exception e) {
                System.out.println("T2 FAILED: " + e.getMessage());
            }
        }, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        return "Check console logs for 'LOCKED' and 'releasing lock' messages. T2 should wait for T1 to finish.";
    }
}
