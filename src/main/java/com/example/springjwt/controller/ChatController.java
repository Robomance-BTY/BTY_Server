//package com.example.springjwt.controller;
//
//
//import com.example.springjwt.model.ChatRoom;
//import com.example.springjwt.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/chat")
//public class ChatController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//    private final ChatService chatService;
//
//    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
//        this.messagingTemplate = messagingTemplate;
//        this.chatService = chatService;
//    }
//
//
//    @PostMapping
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatService.createRoom(name);
//    }
//
//    @GetMapping
//    public List<ChatRoom> findAllRoom() {
//        return chatService.findAllRoom();
//    }
//
//    @MessageMapping("/chat.sendMessage")
//    public void receiveMessage(String message) {
//        // 모든 클라이언트에게 메시지 전송
//        messagingTemplate.convertAndSend("/topic/public", message);
//    }
//}
