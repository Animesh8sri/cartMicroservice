package com.baniya.cart.microservice.service.impl;

import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.dto.EmailDTO;
import com.baniya.cart.microservice.dto.OrderHistoryDTO;
import com.baniya.cart.microservice.dto.UserProfile;
import com.baniya.cart.microservice.entity.Cart;
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

    @Override
    public OrderHistory insert(OrderHistory orderHistory) {
        return orderHistoryRepository.insert(orderHistory);
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MerchantProxy merchantProxy;

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

    @Override
    public void merchantCheckout(Cart cart) {
        merchantProxy.updateInventory(cart);
    }



    @KafkaListener(topics = "Email",groupId = "group_id")
    public void consume(String emailBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO = objectMapper.readValue(emailBody,EmailDTO.class);

        String emailID = emailDTO.getUserEmailId();
        OrderHistoryDTO orderHistoryDTO = emailDTO.getOrderHistoryDTO();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailID);
        message.setSubject("Order History Details from Baniya.com!");
        message.setText(orderHistoryDTO.getCart().getProductDTO().toString());

        javaMailSender.send(message);

    }
}
