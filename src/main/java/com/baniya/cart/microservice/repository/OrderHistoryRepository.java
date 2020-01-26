package com.baniya.cart.microservice.repository;

import com.baniya.cart.microservice.entity.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory,String> {

    public List<OrderHistory> findOrderHistoryByUserId(String customerId);

    public long countCartProductDTOCounterByCartProductDTOProductId(String productId);
    public OrderHistory findOrderHistoryByOrderId(String orderId);
}
