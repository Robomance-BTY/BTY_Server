//package com.example.springjwt.service;
//import com.example.springjwt.dto.PayloadDTO;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class MQTTService implements MqttCallback {
//    // application.properties 파일에서 mqtt.broker.host 항목의 값
//    @Value("${mqtt.broker.host}")
//    private String host;
//
//    // application.properties 파일에서 mqtt.broker.port 항목의 값
//    @Value("${mqtt.broker.port}")
//    private int port;
//
//    private MqttClient client;
//
//    @PostConstruct
//    public void connect(){
//        String serverURI = getURI();
//        String uuid = UUID.randomUUID().toString();
//
//        log.info("MQTT Broker Server : "+serverURI);
//        log.info("UUID : "+uuid);
//
//        MqttConnectOptions option = new MqttConnectOptions();
//        option.setCleanSession(true);
//
//        try {
//            client = new MqttClient(serverURI, uuid);
//            client.setCallback(this);
//            client.connect(option);
//        } catch(MqttException e){
//            log.error("MQTT connection error");
//        }
//    }
//
//    public String getURI(){
//        return "tcp://"+host+":"+ port;
//    }
//
//    public PayloadDTO publish(PayloadDTO payloadDto){
//        log.info("Message is published : "+payloadDto.getTopic()+"/"
//                +payloadDto.getMessage());
//
//        MqttMessage message = new MqttMessage();
//        try {
//            message.setPayload(payloadDto.getMessage().getBytes("UTF-8"));
//            client.publish(payloadDto.getTopic(),message);
//        } catch(Exception e){
//            log.error(e.getMessage());
//        }
//
//        return payloadDto;
//    }
//
//    @Override
//    public void connectionLost(Throwable cause) {
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//    }
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//    }
//}