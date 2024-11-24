package com.backend.domain.chat.handler.event.eventProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Component;

@Component
public class PositionEventProcessor {

    // room의 하나의 오브젝트에서 발생하는 이베트는 시간에 따른 순서가 보장되어야 한다.
    // room들과 오브젝트들의 이벤트는 병렬로 처리되어도 된다.
    // => room의 하나의 오브젝트에 대한 파티션 처리가 필요하다.
    private final Map<Long, Map<String, Queue<Callable<CompletableFuture<JsonNode>>>>> positionEventQueues = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public CompletableFuture<JsonNode> submitEvent(Long roomId, String objectId,
            Callable<CompletableFuture<JsonNode>> event) {
        // set EventQueues
        // roomId에 대한 queue가 없는 경우 새로운 큐를 생성한다.
        // object에 대한 queue가 없는 경우 새로운 링크 큐(순서보장)를 생성한다.
        // queue에 event 등록
        final CompletableFuture<JsonNode> future = new CompletableFuture<>();
        positionEventQueues
                .computeIfAbsent(roomId, id -> new ConcurrentHashMap<>())
                .computeIfAbsent(objectId, id -> new ConcurrentLinkedQueue<>())
                .offer(() -> {
                    try {
                        return event.call().thenApply(result -> {
                            future.complete(result);
                            return result;
                        });
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                });
        processQueue(roomId, objectId);
        return future;
    }

    private synchronized void processQueue(Long roomId, String objectId) {
        Queue<Callable<CompletableFuture<JsonNode>>> queue = positionEventQueues.get(roomId)
                .get(objectId);

        Callable<CompletableFuture<JsonNode>> event = queue.poll();
        executorService.submit(() -> {
            try {
                assert event != null;
                event.call();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            } finally {
                processQueue(roomId, objectId);
            }
        });

    }
}
