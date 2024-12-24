package com.backend.domain.chat.handler.event.eventProcessor;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PositionEventProcessor {

    private final EventQueueManager<JsonNode> queueManager;

    @Autowired
    public PositionEventProcessor(
            final EventQueueManager<JsonNode> queueManager
    ) {
        this.queueManager = queueManager;
    }

    @Async("taskExecutor")
    public CompletableFuture<JsonNode> submitEvent(final Long objectId,
            final Supplier<JsonNode> eventSupplier) {
        return queueManager.submitEvent(objectId, eventSupplier);
    }
}
