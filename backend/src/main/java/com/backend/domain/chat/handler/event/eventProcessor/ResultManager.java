package com.backend.domain.chat.handler.event.eventProcessor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ResultManager<T> {
    private final ConcurrentHashMap<Long, CompletableFuture<T>> resultMap = new ConcurrentHashMap<>();

    public void addResult(Long key, CompletableFuture<T> resultFuture) {
        resultMap.put(key, resultFuture);
    }

    public CompletableFuture<T> getResult(Long key) {
        return resultMap.computeIfAbsent(key, k -> new CompletableFuture<>());
    }
}
