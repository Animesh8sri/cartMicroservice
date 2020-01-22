package com.baniya.cart.microservice.repository;

import com.baniya.cart.microservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart,String> {
    List<Cart> findAll();
    List<Cart> findByUserId(String userId);
}
