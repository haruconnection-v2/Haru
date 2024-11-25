package com.backend.domain.chat.handler.event.eventProcessor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class EventProcessor<T> {
    private static final Integer NUM_THREAD = 16;
    private final ExecutorService executorService;

    public EventProcessor() {
        this.executorService = Executors.newFixedThreadPool(NUM_THREAD);
    }

    public CompletableFuture<T> processEvent(Supplier<T> eventSupplier) {
        return CompletableFuture.supplyAsync(eventSupplier, executorService);
    }
}
