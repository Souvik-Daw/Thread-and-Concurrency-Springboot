package com.example.TandC;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "myExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);          // minimum threads always alive
        executor.setMaxPoolSize(10);          // maximum threads allowed
        executor.setQueueCapacity(20);       // tasks to keep in queue
        executor.setThreadNamePrefix("my-pool-");

        executor.initialize();
        return executor;
    }
}