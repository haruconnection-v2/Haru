package com.backend.global.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
    private final Map<Object, Object> sessionIdMap = new ConcurrentHashMap<>();

    // 일단 임시로 만듦. 세션 연결 시, roodId를 받아 인원 수를 관리하도록 구현할 것임.
    // diaryId를 조회하여 내역들을 불러온다.
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        // 클라이언트가 연결 시 roomId를 세션에서 가져오기
        // String roomId = (String)Objects.requireNonNull(accessor.getSessionAttributes()).get("roomId");
        // log.info("User connected to room: " + roomId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // 연결 해제 시 roomId 확인
        String roomId = (String) accessor.getSessionAttributes().get("roomId");
        log.info("User disconnected from room: " + roomId);

        // if (roomId != null) {
        // 	// 사용자가 속한 roomId의 카운트를 감소시킴
        // }
    }

//    @EventListener
//    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
//        Object sessionId = Objects.requireNonNull(event.getMessage().getHeaders().get("simpSessionId"));
//        Object userId = Objects.requireNonNull(event.getUser()).getName();
//        sessionIdMap.put(sessionId, userId);
//    }
//
//    @EventListener
//    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
//        Object sessionId = event.getSessionId();
//        sessionIdMap.remove(sessionId);
//    }
}
