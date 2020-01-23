package com.baniya.cart.microservice.controller;

import com.baniya.cart.microservice.controller.impl.APIProxy;
import com.baniya.cart.microservice.dto.EmailDTO;
import com.baniya.cart.microservice.dto.OrderHistoryDTO;
import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.dto.UserProfile;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.entity.OrderHistory;
import com.baniya.cart.microservice.service.CartService;
import com.baniya.cart.microservice.service.OrderHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderHistoryController {

    @Autowired
    CartService service;

    @Autowired
    APIProxy apiProxy;

    @Autowired
    OrderHistoryService orderHistoryService;


    @GetMapping("/checkout")
    public List<ProductDTO> checkout(@RequestHeader HttpHeaders headers)
    {
        OrderHistory orderHistory=new OrderHistory();
        OrderHistory orderCheckout;
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
        ProductDTO productDTO = new ProductDTO();
        List<Cart> userIdList = service.findByUser(userProfile.getId());


        List<String> productIds = userIdList.stream().map(Cart::getProductId).collect(Collectors.toList());
        List<ProductDTO> products = new ArrayList<>();
        products = service.viewProductsByProductIds(productIds);
        Map<String, ProductDTO> map = new HashMap<>();
        for(ProductDTO product: products){
            if (null != product.getProductId()) {
                map.put(product.getProductId(), product);
            }
        }

        service.viewStockAndPrice(userIdList, map);
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
        orderHistoryDTO.setProducts(products);
        orderHistoryDTO.setUserId(userProfile.getId());
        String userEmailId = userProfile.getEmail();

        BeanUtils.copyProperties(orderHistoryDTO,orderHistory);
        orderCheckout = orderHistoryService.insert(orderHistory);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setOrderHistoryDTO(orderHistoryDTO);
        emailDTO.setUserEmailId(userProfile.getEmail());

        orderHistoryService.kafkaConsumer(emailDTO);
        return products;
    }


}
