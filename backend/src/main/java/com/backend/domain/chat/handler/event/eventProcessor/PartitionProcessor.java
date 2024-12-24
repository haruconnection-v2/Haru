package com.backend.domain.chat.handler.event.eventProcessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PartitionProcessor<T> {
    @Async("eventTaskExecutor")
    public CompletableFuture<T> processPartition(final BlockingQueue<Supplier<T>> queue) {
        final Supplier<T> eventSupplier = queue.poll();
        if (eventSupplier == null) {
            return CompletableFuture.completedFuture(null);
        }
        log.info("processPartition: {}", eventSupplier);
        return CompletableFuture.supplyAsync(eventSupplier);
    }
}
