package com.example.springjwt.service;

import com.example.springjwt.dto.MessageDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // 연결된 클라이언트 정보 로그
        System.out.println("새 웹소켓 연결이 감지되었습니다: " + headerAccessor.getSessionId());
        // 여기서 연결된 사용자 정보나 기타 정보를 처리할 수 있습니다.

        // 연결 메시지 전송 예제
        MessageDTO message = MessageDTO.builder()
                .type("new")
                .sender("server")
                .channelId("system")
                .data("새 연결이 감지되었습니다.")
                .build();
        messagingTemplate.convertAndSend("/sub/channel/system", message);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // 연결 해제된 클라이언트 정보 로그
        System.out.println("웹소켓 연결이 해제되었습니다: " + headerAccessor.getSessionId());
        // 연결 해제 처리
        // 예를 들어, 사용자 리스트에서 해당 세션을 제거하는 등의 작업을 할 수 있습니다.

        // 연결 해제 메시지 전송 예제
        MessageDTO message = MessageDTO.builder()
                .type("close")
                .sender("server")
                .channelId("system")
                .data("연결이 해제되었습니다.")
                .build();
        messagingTemplate.convertAndSend("/sub/channel/system", message);
    }
}
