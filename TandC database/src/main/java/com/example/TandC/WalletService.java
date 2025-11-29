package com.example.TandC;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository repo;

    public WalletService(WalletRepository repo) {
        this.repo = repo;
    }

    /**
     * Optimistic Locking: Uses @Version to detect conflicts at commit time
     */
    @Transactional
    public void addMoney(Long id, int amount) {
        Wallet w = repo.findById(id).orElseThrow();

        System.out.println("Thread " + Thread.currentThread().getName() +
                " read version: " + w.getVersion());

        // Simulate slow processing
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }

        w.setBalance(w.getBalance() + amount);

        // When transaction commits → optimistic lock check happens
    }

    /**
     * Pessimistic Locking: Locks the row at database level (SELECT ... FOR UPDATE)
     * Other threads will WAIT until this transaction completes
     */
    @Transactional
    public void addMoneyWithPessimisticLock(Long id, int amount) {
        Wallet w = repo.findByIdWithPessimisticLock(id).orElseThrow();

        System.out.println("Thread " + Thread.currentThread().getName() +
                " LOCKED wallet with version: " + w.getVersion());

        // Simulate slow processing - row remains locked during this time
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }

        w.setBalance(w.getBalance() + amount);

        System.out.println("Thread " + Thread.currentThread().getName() +
                " releasing lock and committing");

        // Lock is released when transaction commits
    }
}
