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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/order")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class OrderHistoryController {

    @Autowired
    CartService cartService;

    @Autowired
    APIProxy apiProxy;

    @Autowired
    OrderHistoryService orderHistoryService;

    @Autowired
    MerchantProxy merchantProxy;


    @GetMapping("/checkout")
    public ResponseEntity<ResponseToCart> checkout(@RequestHeader HttpHeaders headers)
    {
        try {
            Object data = headers.get("guest");
            if(headers.get("isWeb").get(0).equals("true"))
            {
                data=null;
            }
            boolean isGuest = false;
            ResponseToCart responseToCart = new ResponseToCart();
            OrderHistory orderHistory = new OrderHistory();
            OrderHistoryDTO orderCheckout = new OrderHistoryDTO();
            String userIdHeader = headers.get("Auth").get(0);
            UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
            Optional<Cart> guestCart = Optional.of(new Cart());
            if (data != null) {
                String guestId = headers.get("guest").get(0);
                guestCart = cartService.findCartByCartId(guestId);
                isGuest = true;
            }
            if (isGuest && guestCart.isPresent()) {
                guestCart.get().setCartId(userProfile.getId());
                cartService.save(guestCart);
                cartService.deleteById(headers.get("guest").get(0));
            }
            Cart cartCheckout = cartService.findCartByCartId(userProfile.getId()).get();
            ProductDTO productDTO = new ProductDTO();
            orderCheckout.setCart(cartCheckout);
            orderCheckout.setUserId(userProfile.getId());
            Date date = new Date();
            orderCheckout.setTimestamp(new Timestamp(date.getTime()));
            BeanUtils.copyProperties(orderCheckout, orderHistory);


            CheckoutCartDTO checkoutCartDTO = merchantProxy.updateInventory(cartCheckout);
            if (checkoutCartDTO.isSuccess()) {
                responseToCart.setIsSuccess(true);
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setOrderHistoryDTO(orderCheckout);
                emailDTO.setUserEmailId(userProfile.getEmail());
                responseToCart.setCartId(userProfile.getId());
                orderHistoryService.kafkaConsumer(emailDTO);
                orderHistoryService.insert(orderHistory);
                cartService.deleteCart(cartCheckout);
            } else {
                responseToCart.setIsSuccess(false);
                responseToCart.setErrorMessage("Some items are out of stock!");
                List<String> productOutOfStock = checkoutCartDTO.getProductId();
                cartCheckout.setProductDTO(cartCheckout.getProductDTO().stream().filter(productDTO1 -> !productOutOfStock.contains(productDTO1.getProductId())).collect(Collectors.toList()));
                cartService.save(Optional.of(cartCheckout));
            }

            return new ResponseEntity(responseToCart, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/productCount/{productId}")
    public long viewProductCount(@PathVariable("productId") String productId)
    {
        return orderHistoryService.sumCounterByProductId(productId);
    }

    @GetMapping("/customer/{merchantId}")
    public List<ProductDTO> merchantViewCustomer(@PathVariable("merchantId") String merchantId)
    {
        return orderHistoryService.merchantViewCustomer(merchantId);
    }

    @GetMapping("/orderHistory")
    public List<OrderHistory> viewCustomerOrderHistory(@RequestHeader HttpHeaders headers)
    {
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
        String customerId = userProfile.getId();

        OrderHistory orderHistory = new OrderHistory();
        return orderHistoryService.findOrderHistoryByUserId(customerId);
    }

    @GetMapping("/orderId/{orderId}")
    public List<ProductDTO> viewCustomerOrderDetails(@PathVariable("orderId") String orderId)
    {
        return orderHistoryService.findOrderHistoryByOrderId(orderId);
    }

}
