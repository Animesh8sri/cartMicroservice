package com.baniya.cart.microservice.repository;

import com.baniya.cart.microservice.entity.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderHistoryRepository extends MongoRepository<OrderHistory,String> {
}
