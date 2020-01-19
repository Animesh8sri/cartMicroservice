package com.baniya.cart.microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderHistoryRepository extends MongoRepository<String,String> {
}
