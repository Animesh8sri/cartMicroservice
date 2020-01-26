package com.baniya.cart.microservice.service;

import com.baniya.cart.microservice.dto.EmailDTO;
import com.baniya.cart.microservice.dto.MerchantViewCustomerDTO;
import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.entity.OrderHistory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderHistoryService {

    OrderHistory insert(OrderHistory orderHistory);
    public void kafkaConsumer(EmailDTO emailDTO);
    public void merchantCheckout(Cart cart);
    public List<ProductDTO> merchantViewCustomer(String merchantId);
    List<OrderHistory> findOrderHistoryByUserId(String customerId);
    public long sumCounterByProductId(String productId);
    List<ProductDTO> findOrderHistoryByOrderId(String orderId);

}
