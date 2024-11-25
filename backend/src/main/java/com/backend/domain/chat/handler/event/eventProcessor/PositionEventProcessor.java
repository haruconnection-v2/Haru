package com.backend.domain.chat.handler.event.eventProcessor;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PositionEventProcessor {

    private final EventQueueManager<CompletableFuture<JsonNode>> queueManager;
    private final ConcurrentHashMap<Long, CompletableFuture<Void>> processingMap = new ConcurrentHashMap<>();


    @Autowired
    public PositionEventProcessor(
            EventQueueManager<CompletableFuture<JsonNode>> queueManager
    ) {
        this.queueManager = queueManager;
    }

    public CompletableFuture<JsonNode> submitEvent(Long objectId,
            Supplier<CompletableFuture<JsonNode>> eventSupplier) {
        CompletableFuture<JsonNode> resulFuture = new CompletableFuture<>();
        queueManager.addEvent(objectId, () -> handleEvent(eventSupplier, resulFuture));
        processQueue(objectId);
        return resulFuture;
    }

    private CompletableFuture<JsonNode> handleEvent(
            Supplier<CompletableFuture<JsonNode>> eventSupplier,
            CompletableFuture<JsonNode> resulFuture) {
        CompletableFuture<JsonNode> eventResult = eventSupplier.get();
        completeEvent(eventResult, resulFuture);
        return eventResult;
    }

    private void processQueue(Long objectId) {
        Supplier<CompletableFuture<JsonNode>> eventSupplier = queueManager.pollEvent(objectId);
        if (eventSupplier == null) {
            return;
        }
        CompletableFuture<Void> previousFuture = processingMap.getOrDefault(objectId,
                CompletableFuture.completedFuture(null));

        CompletableFuture<Void> currentFuture = previousFuture.thenComposeAsync(v -> {
            CompletableFuture<JsonNode> eventResult = eventSupplier.get();
            return eventResult.thenAccept(
                            result -> log.info("Processed event for object {} : {}", objectId, result))
                    .exceptionally(ex -> {
                        log.info("Error processing event for object {} : {}", objectId,
                                ex.getMessage());
                        return null;
                    });
        });
        processingMap.put(objectId, currentFuture);
        currentFuture.whenComplete((v, ex) -> processQueue(objectId));
    }

    private void completeEvent(CompletableFuture<JsonNode> eventResult,
            CompletableFuture<JsonNode> resultFuture) {
        eventResult.whenComplete((result, ex) -> {
            if (ex != null) {
                resultFuture.completeExceptionally(ex);
            } else {
                resultFuture.complete(result);
            }
        });
    }
}
