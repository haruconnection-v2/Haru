package com.backend.domain.chat.handler.event.eventProcessor;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionEventProcessorTest {

    private PositionEventProcessor positionEventProcessor;


    @BeforeEach
    void setUp() {
        EventQueueManager<CompletableFuture<JsonNode>> queueManager = new EventQueueManager<>();
        positionEventProcessor = new PositionEventProcessor(queueManager);
    }

    @DisplayName("하나의 오브젝트에 이벤트가 순차적으로 처리된다.")
    @Test
    void testSequentialProcessingInSameObject() throws Exception {
        final Long objectId = 1L;
        final AtomicInteger eventCounter = new AtomicInteger();
        final AtomicInteger timeCounter = new AtomicInteger(0);

        CompletableFuture<JsonNode> firstFuture = positionEventProcessor.submitEvent(objectId,
                () -> CompletableFuture.supplyAsync(() -> {
                    while (timeCounter.get() < 100000000) {
                        timeCounter.incrementAndGet();
                    }
                    assertEquals(1, eventCounter.incrementAndGet());
                    return TextNode.valueOf("firstFuture");
                })
        );

        CompletableFuture<JsonNode> secondFuture = positionEventProcessor.submitEvent(objectId,
                () -> CompletableFuture.supplyAsync(() -> {
                    assertEquals(2, eventCounter.incrementAndGet());
                    return TextNode.valueOf("secondFuture");
                })
        );

        JsonNode result1 = firstFuture.get();
        JsonNode result2 = secondFuture.get();

        Assertions.assertThat(result1.asText()).isEqualTo("firstFuture");
        Assertions.assertThat(result2.asText()).isEqualTo("secondFuture");
        Assertions.assertThat(eventCounter.get()).isEqualTo(2);
    }

    @DisplayName("서로 다른 오브젝트는 병렬로 처리된다.")
    @Test
    void parallelProcessingWithDifferentObjects() throws Exception {
        final Long objectId1 = 1L;
        final Long objectId2 = 2L;
        final AtomicInteger eventCounter = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<JsonNode> firstFuture = positionEventProcessor.submitEvent(objectId1,
                () -> CompletableFuture.supplyAsync(() -> {
                    try {
                        latch.await();
                        assertEquals(2, eventCounter.incrementAndGet());
                        return TextNode.valueOf("firstFuture");
                    }catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
        );

        CompletableFuture<JsonNode> secondFuture = positionEventProcessor.submitEvent(objectId2,
                () -> CompletableFuture.supplyAsync(() -> {
                    assertEquals(1, eventCounter.incrementAndGet());
                    latch.countDown();
                    return TextNode.valueOf("secondFuture");
                })
        );

        JsonNode result2 = secondFuture.get();
        JsonNode result1 = firstFuture.get();


        Assertions.assertThat(result1.asText()).isEqualTo("firstFuture");
        Assertions.assertThat(result2.asText()).isEqualTo("secondFuture");
        Assertions.assertThat(eventCounter.get()).isEqualTo(2);
    }
}