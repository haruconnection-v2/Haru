package com.backend.domain.chat.service;

import com.backend.domain.chat.dto.request.PersistenceTextBoxReq;
import com.backend.domain.chat.dto.request.PositionEventReq;
import com.backend.domain.chat.dto.request.TextDeleteEventReq;
import com.backend.domain.chat.dto.request.TextInputEventReq;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public interface WebSocketService {

    public void initializeHandlers();

    public CompletableFuture<JsonNode> registerHandler(Long roomId, String type,
            Map<String, JsonNode> payload);

    void positionProcess(Long roomId, PositionEventReq positionEventReq);

    void textInputProcess(Long roomId, TextInputEventReq textInputEventReq);

    void textCreateProcess(final Long roomId, final PersistenceTextBoxReq persistenceTextBoxReq);

    void textSaveProcess(final Long roomId, final PersistenceTextBoxReq persistenceTextBoxReq);

    void textDeleteProcess(final Long roomId, final TextDeleteEventReq textDeleteEventReq);
}
