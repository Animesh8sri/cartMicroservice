package com.baniya.cart.microservice.repository;

import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart,String> {
    List<Cart> findAll();
    Optional<Cart> findCartByCartId(String cartId);
}
