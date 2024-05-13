//package com.example.springjwt.handler;
//
//import com.example.springjwt.model.ChatMessage;
//import com.example.springjwt.model.ChatRoom;
//import com.example.springjwt.service.ChatService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSockChatHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final ChatService chatService;
//    // 세션 ID와 사용자 ID의 맵핑 정보를 저장
//    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
//    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        URI uri = session.getUri();
//        if (uri != null) {
//            String query = uri.getQuery();
//            if (query != null) {
//                // 쿼리가 null이 아닌 경우에만 분할 수행
//                String[] params = query.split("&");
//                // params를 사용한 로직 처리
//            } else {
//                // 쿼리가 null인 경우의 로직 처리 (예: 에러 로깅, 기본값 설정 등)
//            }
//        } else {
//            // URI가 null인 경우의 로직 처리
//        }
//    }
//
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
//        String userId = sessionUserMap.remove(session.getId());
//        if (userId != null) {
//            userSessions.remove(userId);
//            log.info("Session removed: {}, for user: {}", session.getId(), userId);
//        }
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("payload {}", payload);
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
//        room.handleActions(session, chatMessage, chatService);
//    }
//
//    // 사용자 ID를 사용해 클라이언트 세션에 메시지 보내는 메소드
//    public void sendMessageToSession(String userId, String message) {
//        WebSocketSession session = userSessions.get(userId);
//        if (session != null && session.isOpen()) {
//            try {
//                session.sendMessage(new TextMessage(message));
//                log.info("Message sent: {} to user ID: {}", message, userId);
//            } catch (IOException e) {
//                log.error("Error when sending message to user ID: {}", userId, e);
//            }
//        }
//    }
//}
//
