package com.example.springjwt.service;

import com.example.springjwt.dto.MessageDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//@Slf4j
//@Service
//public class MessageService {
//
//    private final SimpMessageSendingOperations simpMessageSendingOperations;
//
//    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations) {
//        this.simpMessageSendingOperations = simpMessageSendingOperations;
//    }
//
//
//    public void sendLocationInfoToSubscribers(String locationInfo, Long userId) {
//        Map<String, Object> messageData = new HashMap<>();
//        messageData.put("userId", userId);
//
//        if (locationInfo != null) {
//            messageData.put("locationInfo", locationInfo);
//        } else {
//            messageData.put("message", "It has been returned.");
//        }
//
//        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", messageData);
//    }
//
//
//
//
// }

@Slf4j
@Service
public class MessageService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Getter
    private Map<String, Object> messageMap = new HashMap<>();

    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void sendLocationInfoToSubscribers(String locationInfo, Long userId) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("userId", userId);

        if (locationInfo != null) {
            messageData.put("locationInfo", locationInfo);
        } else {
            messageData.put("message", "It has been returned.");
        }

        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", messageData);
    }

    // 새로 추가된 메소드
    public void logAndStoreMessage(String locker) {
        messageMap.put("book_locker_state", locker);
        // 로그 출력
        log.info("book_locker_state = {}", messageMap);

    }

}