package com.baniya.cart.microservice.repository;

import com.baniya.cart.microservice.entity.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderHistoryRepository extends MongoRepository<OrderHistory,String> {

    public List<OrderHistory> findOrderHistoryByUserId(String customerId);
}
