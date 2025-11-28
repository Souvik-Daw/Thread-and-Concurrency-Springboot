package com.example.TandC;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Async("myExecutor")
    public void sendEmail(int id) {
        System.out.println("Sending email " + id +
                " on thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(2000); // simulate processing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}