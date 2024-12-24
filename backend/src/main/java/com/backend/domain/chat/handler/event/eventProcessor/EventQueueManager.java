package com.backend.domain.chat.handler.event.eventProcessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventQueueManager<T> {

    private final ConcurrentHashMap<Integer, BlockingQueue<Supplier<T>>> partitions = new ConcurrentHashMap<>();
    private final PartitionProcessor<T> partitionProcessor;
    private final ThreadPoolTaskExecutor executor;

    @Autowired
    public EventQueueManager(PartitionProcessor<T> partitionProcessor,
            @Qualifier(value = "eventTaskExecutor") ThreadPoolTaskExecutor executor) {
        this.partitionProcessor = partitionProcessor;
        this.executor = executor;
        initializePartitions(executor.getCorePoolSize());
    }

    private void initializePartitions(final int partitionCount) {
        for (int i = 0; i < partitionCount; i++) {
            partitions.put(i, new LinkedBlockingQueue<>());
        }
    }

    public CompletableFuture<T> submitEvent(final Long objectId, final Supplier<T> eventSupplier) {
        final int partitionKey = calculatePartition(objectId);
        final BlockingQueue<Supplier<T>> queue = partitions.computeIfAbsent(partitionKey,
                key -> new LinkedBlockingQueue<>());
        final boolean result = queue.offer(eventSupplier);
        log.info("SubmitEvent: {}", eventSupplier);
        if (!result) {
            throw new IllegalArgumentException("");
        }
        return partitionProcessor.processPartition(queue);
    }

    private int calculatePartition(final Long objectId) {
        return Math.toIntExact(objectId % executor.getCorePoolSize());
    }
}
