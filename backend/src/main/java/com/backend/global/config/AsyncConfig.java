package com.backend.global.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "eventTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 초기 스레드 수
        executor.setMaxPoolSize(100); // 최대 스레드 수
        executor.setQueueCapacity(50); // 작업 큐 크기
        executor.setThreadNamePrefix("custom-"); // 스레드 이름 프리픽스
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 작업 거부 시 처리 정책
        executor.setAllowCoreThreadTimeOut(true); // 유휴 상태의 코어 스레드 제거 허용
        executor.setKeepAliveSeconds(60); // 유휴 상태 스레드 제거 대기 시간 설정
        executor.initialize();
        System.out.println("Executor created: " + executor);
        return executor;
    }
}
