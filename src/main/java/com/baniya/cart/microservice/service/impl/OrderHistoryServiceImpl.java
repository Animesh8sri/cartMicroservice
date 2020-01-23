package com.baniya.cart.microservice.service.impl;

import com.baniya.cart.microservice.dto.EmailDTO;
import com.baniya.cart.microservice.dto.OrderHistoryDTO;
import com.baniya.cart.microservice.dto.UserProfile;
import com.baniya.cart.microservice.entity.OrderHistory;
import com.baniya.cart.microservice.repository.OrderHistoryRepository;
import com.baniya.cart.microservice.service.OrderHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService
{
    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public OrderHistory insert(OrderHistory orderHistory) {
        return orderHistoryRepository.insert(orderHistory);
    }

    @Override
    public void kafkaConsumer(EmailDTO emailDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        String emailBody =null;
        try {
            emailBody = objectMapper.writeValueAsString(emailDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.kafkaTemplate.send("Email",emailBody);
    }

    @KafkaListener(topics = "Email",groupId = "group_id")
    public void consume(String emailBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EmailDTO emailDTO = new EmailDTO();
        objectMapper.readValue(emailBody,EmailDTO.class);

        String emailID = emailDTO.getUserEmailId();
        OrderHistoryDTO orderHistoryDTO = emailDTO.getOrderHistoryDTO();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailID);
        message.setSubject("Order History Details from Baniya.com!");
        message.setText(orderHistoryDTO.toString());

        javaMailSender.send(message);
    }
}
