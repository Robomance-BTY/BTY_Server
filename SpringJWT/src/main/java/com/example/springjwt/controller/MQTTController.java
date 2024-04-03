//package com.example.springjwt.controller;
//
//import com.example.springjwt.dto.PayloadDTO;
//import com.example.springjwt.service.MQTTService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
//@Controller
//public class MQTTController {
//    private final MQTTService mqttService;
//
//    @Autowired
//    public MQTTController(MQTTService mqttService){
//        this.mqttService = mqttService;
//    }
//
//    @RequestMapping(value="/message",method=RequestMethod.POST)
//    public @ResponseBody PayloadDTO publish(@RequestBody PayloadDTO payloadDto){
//        return mqttService.publish(payloadDto);
//    }
//}