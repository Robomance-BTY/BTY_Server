package com.example.springjwt.service;

import com.example.springjwt.dto.MessageDTO;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void sendLocationInfoToSubscribers(String locationInfo) {
        MessageDTO message = new MessageDTO();
        message.setData(Map.of("locationInfo", locationInfo));
        simpMessageSendingOperations.convertAndSend("/sub/channel/bookinfo", message.getData());
    }

}
