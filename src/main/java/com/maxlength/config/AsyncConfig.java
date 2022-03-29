package com.maxlength.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
    import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

    @Configuration
    @EnableAsync
    public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);     // 최초 생성되는 쓰레드 갯수
        threadPoolTaskExecutor.setMaxPoolSize(100);     // 최대 쓰레드 갯수
        threadPoolTaskExecutor.setQueueCapacity(500);   // 큐 사이즈
        threadPoolTaskExecutor.setThreadNamePrefix("ethereum-async-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
    }
