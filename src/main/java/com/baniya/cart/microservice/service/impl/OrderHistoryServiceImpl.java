package com.baniya.cart.microservice.service.impl;

import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.entity.OrderHistory;
import com.baniya.cart.microservice.repository.OrderHistoryRepository;
import com.baniya.cart.microservice.service.OrderHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ProductDTO> merchantViewCustomer(String merchantId) {
        List<OrderHistory> orderHistory = orderHistoryRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        List<OrderHistory> filteredList = orderHistory.stream().filter(orderHistory1 -> orderHistory1.getCart().getProductDTO().stream().allMatch(s -> s.getMerchantId().equals(merchantId))).collect(Collectors.toList());
        filteredList.forEach(orderHistory1 -> {
            productDTOList.addAll(orderHistory1.getCart().getProductDTO());
        });
        return productDTOList;
    }

    @Override
    public List<OrderHistory> findOrderHistoryByUserId(String customerId) {
        List<OrderHistory> orders = orderHistoryRepository.findAll();
        List<OrderHistory> ordersOfCustomer = orders.stream().filter(orders1 -> orders1.getUserId().equals(customerId)).collect(Collectors.toList());
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MONTH, 6);

        Date expiryDate = cal.getTime();
        List<OrderHistory> orderFilter = ordersOfCustomer.stream().filter(orders2 -> orders2.getTimestamp().compareTo(expiryDate) <0).collect(Collectors.toList());

        return orderFilter;
    }

    @Override
    public long sumCounterByProductId(String productId) {

        return orderHistoryRepository.countCartProductDTOCounterByCartProductDTOProductId(productId);
    }

    @Override
    public List<ProductDTO> findOrderHistoryByOrderId(String orderId) {
        OrderHistory orders = orderHistoryRepository.findOrderHistoryByOrderId(orderId);
        List<ProductDTO> products = orders.getCart().getProductDTO();
        return products;
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
