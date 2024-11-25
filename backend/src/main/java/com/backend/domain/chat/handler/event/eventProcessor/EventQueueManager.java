package com.backend.domain.chat.handler.event.eventProcessor;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class EventQueueManager<T> {

    private final Map<Long, Queue<Supplier<T>>> eventQueues = new ConcurrentHashMap<>();

    protected void addEvent(Long partitionKey,
            Supplier<T> eventSupplier) {
        Queue<Supplier<T>> queue = eventQueues.computeIfAbsent(partitionKey,
                key -> new ConcurrentLinkedQueue<>());
        queue.offer(eventSupplier);
    }

    public Supplier<T> pollEvent(Long partitionKey) {
        Queue<Supplier<T>> queue = eventQueues.get(partitionKey);
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }
}
