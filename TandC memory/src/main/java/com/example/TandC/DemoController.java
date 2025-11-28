package com.example.TandC;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final EmailService emailService;

    @Autowired
    @Qualifier("myExecutor")
    ThreadPoolTaskExecutor executor;

    public DemoController(EmailService emailService) {
        this.emailService = emailService;
    }

    //background async tasks
    @GetMapping("/send")
    public String callAsyncTasks() {
        for (int i = 1; i <= 20; i++) {
            emailService.sendEmail(i);
        }
        return "20 async tasks submitted!";
    }

    //Parallel tasks with Future, recommended for CPU intensive tasks and prods
    @GetMapping("/parallel")
    public String runParallel() throws Exception {

        Future<String> f1 = executor.submit(() -> callApi("A"));
        Future<String> f2 = executor.submit(() -> callApi("B"));

        return f1.get() + " | " + f2.get();  // waits
    }

    private String callApi(String name) {
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        return "API " + name + " DONE";
    }
}