package com.baniya.cart.microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<String,String> {
}
