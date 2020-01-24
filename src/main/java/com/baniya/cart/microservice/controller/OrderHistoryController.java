package com.baniya.cart.microservice.controller;

import com.baniya.cart.microservice.controller.impl.APIProxy;
import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.entity.OrderHistory;
import com.baniya.cart.microservice.service.CartService;
import com.baniya.cart.microservice.service.OrderHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
public class OrderHistoryController {

    @Autowired
    CartService service;

    @Autowired
    APIProxy apiProxy;

    @Autowired
    OrderHistoryService orderHistoryService;

    @Autowired
    MerchantProxy merchantProxy;


    @GetMapping("/checkout")
    public ResponseEntity<ResponseToCart> checkout(@RequestHeader HttpHeaders headers)
    {
        OrderHistory orderHistory=new OrderHistory();
        OrderHistoryDTO orderCheckout = new OrderHistoryDTO();
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
        Cart cartCheckout = service.findCartByCartId(userProfile.getId()).get();
        ProductDTO productDTO = new ProductDTO();

        orderCheckout.setCart(cartCheckout);
        orderCheckout.setUserId(userProfile.getId());


        BeanUtils.copyProperties(orderCheckout,orderHistory);
        orderHistoryService.insert(orderHistory);

        CheckoutCartDTO checkoutCartDTO = merchantProxy.updateInventory(cartCheckout);


        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setOrderHistoryDTO(orderCheckout);
        emailDTO.setUserEmailId(userProfile.getEmail());
        ResponseToCart responseToCart = new ResponseToCart();
        responseToCart.setCartId(userProfile.getId());

        orderHistoryService.kafkaConsumer(emailDTO);

        service.deleteCart(cartCheckout);
        return new ResponseEntity<ResponseToCart>(responseToCart,HttpStatus.CREATED);
    }


}
