package com.baniya.cart.microservice.service;

import com.baniya.cart.microservice.dto.EmailDTO;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.entity.OrderHistory;

public interface OrderHistoryService {

    OrderHistory insert(OrderHistory orderHistory);
    public void kafkaConsumer(EmailDTO emailDTO);
    public void merchantCheckout(Cart cart);

}
