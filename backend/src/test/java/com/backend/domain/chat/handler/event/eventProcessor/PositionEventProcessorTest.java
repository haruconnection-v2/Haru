package com.backend.domain.chat.handler.event.eventProcessor;
/*

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;

import com.backend.domain.chat.handler.PositionEventHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class PositionEventProcessorTest {

    private PositionEventProcessor positionEventProcessor;
    @Mock
    private PositionEventHandler positionEventHandler;

    @BeforeEach
    void setUp() {
        PartitionProcessor<JsonNode> partitionProcessor = new PartitionProcessor<>();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("test-");
        executor.initialize();
        EventQueueManager<JsonNode> queueManager = new EventQueueManager<>(partitionProcessor,
                executor);
        positionEventProcessor = new PositionEventProcessor(queueManager);
    }

    @DisplayName("하나의 오브젝트에 이벤트가 순차적으로 처리된다.")
    @Test
    void testSequentialProcessingInSameObject() throws Exception {
        final Long objectId = 1L;
        final AtomicInteger eventCounter = new AtomicInteger();
        final AtomicInteger timeCounter = new AtomicInteger(0);

        CompletableFuture<JsonNode> firstFuture = positionEventProcessor.submitEvent(objectId,
                () -> {
                    assertEquals(1, eventCounter.incrementAndGet());
                    return TextNode.valueOf("firstFuture");
                }
        );

        CompletableFuture<JsonNode> secondFuture = positionEventProcessor.submitEvent(objectId,
                () -> {
                    assertEquals(2, eventCounter.incrementAndGet());
                    return TextNode.valueOf("secondFuture");
                }
        );

        JsonNode result1 = firstFuture.get();
        JsonNode result2 = secondFuture.get();

        Assertions.assertThat(result1.asText()).isEqualTo("firstFuture");
        Assertions.assertThat(result2.asText()).isEqualTo("secondFuture");
        Assertions.assertThat(eventCounter.get()).isEqualTo(2);
    }

    @Test
    void SequentialProcessing() throws Exception {
        final Long objectId = 1L;
        final List<Long> result = new ArrayList<>();
        final AtomicInteger eventCounter = new AtomicInteger(0);
        final List<Long> expectResult = new ArrayList<>();

        for (long i = 0L; i < 100L; i++) {
            CompletableFuture<JsonNode> future = positionEventProcessor.submitEvent(objectId,
                    () -> LongNode.valueOf(eventCounter.incrementAndGet())
            );
            result.add(future.get().longValue());
            expectResult.add(i + 1L);
        }
        Assertions.assertThat(result).containsExactlyElementsOf(expectResult);
    }

    @Test
    void testThreadAndTimestampOrder() {
        final Long objectId = 1L;
        final List<String> processingOrder = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<JsonNode> firstFuture = positionEventProcessor.submitEvent(objectId,
                () -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return TextNode.valueOf("firstEvent");
                });
        CompletableFuture<JsonNode> secondFuture = positionEventProcessor.submitEvent(objectId,
                () -> {
                    latch.countDown();
                    return TextNode.valueOf("secondEvent");
                });

        CompletableFuture.allOf(firstFuture, secondFuture).thenRun(
                () -> {
                    try {
                        processingOrder.add(firstFuture.get().asText());
                        processingOrder.add(secondFuture.get().asText());
                    } catch (Exception e) {
                        throw new RuntimeException("");
                    }
                }
        ).join();

        Assertions.assertThat(processingOrder).containsExactly("firstEvent", "secondEvent");
    }

    @DisplayName("서로 다른 오브젝트는 병렬로 처리된다.")
    @Test
    void parallelProcessingWithDifferentObjects() throws Exception {
        final Long objectId1 = 1L;
        final Long objectId2 = 2L;
        final AtomicInteger eventCounter = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<JsonNode> firstFuture = positionEventProcessor.submitEvent(objectId1,
                () -> {
                    try {
                        latch.await();
                        assertEquals(2, eventCounter.incrementAndGet());
                        return TextNode.valueOf("firstFuture");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        CompletableFuture<JsonNode> secondFuture = positionEventProcessor.submitEvent(objectId2,
                () -> {
                    assertEquals(1, eventCounter.incrementAndGet());
                    latch.countDown();
                    return TextNode.valueOf("secondFuture");
                }
        );

        JsonNode result2 = secondFuture.get();
        JsonNode result1 = firstFuture.get();

        Assertions.assertThat(result1.asText()).isEqualTo("firstFuture");
        Assertions.assertThat(result2.asText()).isEqualTo("secondFuture");
        Assertions.assertThat(eventCounter.get()).isEqualTo(2);
    }
}*/
