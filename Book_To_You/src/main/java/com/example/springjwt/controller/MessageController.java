package com.example.springjwt.controller;

import com.example.springjwt.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@Controller
//public class MessageController {
//
//    private final MessageService messageService;
//
//    public MessageController(MessageService messageService) {
//        this.messageService = messageService;
//    }
//
//    @MessageMapping("/channel/bookstorageinfo")
//    public void receiveAndLogMessage(String locker) {
//        // 예시로 userId를 1로 고정하여 사용
//        messageService.logAndStoreMessage(locker);
//    }
//}


@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/channel/bookstorageinfo")
    public void receiveAndLogMessage(
        @RequestBody Map<String, Object> locker) {
        // 예시로 userId를 1로 고정하여 사용
        messageService.logAndStoreMessage(locker);
    }
}