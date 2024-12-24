import React, {useEffect, useRef} from 'react';
import { Stomp } from '@stomp/stompjs';
import textStore from "../stores/textStore.js";
import useTextStore from "../stores/textStore.js"; // STOMP 사용

const useWebSocket = (diaryId, websocketEventHandler, n) => {
  const stompClient = useRef(null);
  const isFirstConnection = useRef(true);

  console.log("websocket onMessage: ", websocketEventHandler);

  useEffect(() => {
    if (!diaryId) return;

    //const client = Stomp.client(`ws://127.0.0.1:8080/ws`);
    const client = Stomp.client(`ws://44.215.101.154:8080/ws`);
    stompClient.current = client;

    client.connect({}, () => {
      console.log('WebSocket 연결됨');
      client.subscribe(`/harurooms/${diaryId}`, (message) => {
        const data = JSON.parse(message.body);
        websocketEventHandler(data, n);
        console.log("textStore: " + JSON.stringify(useTextStore.getState().texts));
      });
      client.subscribe('/user/')
    });

    return () => {
      client.disconnect();
      console.log('WebSocket 연결 종료');
    };
  }, [diaryId, websocketEventHandler]);

  return stompClient;
};

export default useWebSocket;