package com.example.springjwt.service;

import com.example.springjwt.dto.MessageDTO;
import com.example.springjwt.dto.RentalDTO;
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
    private Map<String, Object> messageData = new HashMap<>();
    @Getter
    private String assignedRegion = null;

    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

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

    public void rent(String locationInfo, Long userId, Long bookId, String assignedRegion) {
        messageData.put("bookLocation", locationInfo);
        messageData.put("userLocation", userId);
        messageData.put("BookId", bookId);
        messageData.put("assignedRegion", assignedRegion);
        log.info("Send STOMP message to MobilePlatform = {}",messageData);
        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", messageData);
        messageData.clear();
    }

    public void bookReturn(Long userId, Long bookId, String assignedRegion) {
        messageData.put("userLocation", userId);
        messageData.put("BookId", bookId);
        messageData.put("assignedRegion", assignedRegion);
        log.info("Send STOMP message to MobilePlatform = {}",messageData);
        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", messageData);
        messageData.clear();
    }

    public void reservationReturn(Long startPoint, Long endPoint, Long bookId, String assignedRegion) {
        messageData.put("startPoint", startPoint);
        messageData.put("endPoint", endPoint);
        messageData.put("BookId", bookId);
        messageData.put("assignedRegion", assignedRegion);
        log.info("Send STOMP message to MobilePlatform = {}",messageData);
        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", messageData);
        messageData.clear();
    }

    // 새로 추가된 메소드
    // 새로 추가된 메소드
//    public void logAndStoreMessage(Map<String, Object> locker) {
        public void logAndStoreMessage(Map<String, Object> locker) {
        // messageMap에 locker의 내용을 병합하여 저장
        messageMap.putAll(locker);
        // 로그 출력
        log.info("book_locker_state = {}", messageMap);
    }


    public void assignRegion() {
        for (Map.Entry<String, Object> entry : messageMap.entrySet()) {
            if (entry.getValue() == null) {
                // null 값을 찾으면 assignedRegion에 키 값을 할당하고 반복을 중단합니다.
                assignedRegion = entry.getKey();
                break;
            }
        }
    }
}



//// 결과 출력 (테스트 목적)
//        if (assignedRegion != null) {
//        System.out.println("Assigned Region: " + assignedRegion);
//        } else {
//                System.out.println("No region was assigned.");
//        }
//                }
