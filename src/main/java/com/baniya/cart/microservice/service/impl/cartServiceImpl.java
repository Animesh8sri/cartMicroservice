package com.baniya.cart.microservice.service.impl;


import com.baniya.cart.microservice.controller.impl.CartProxy;
import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.repository.CartRepository;
import com.baniya.cart.microservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class cartServiceImpl implements CartService {
    @Autowired
    CartProxy cartProxy;

    @Autowired
    MerchantProxy merchantProxy;

    @Autowired
    CartRepository cartRepository;



    @Override
    public Cart insert(Cart cart) {
        return cartRepository.insert(cart);
    }

    @Override
    public Optional<Cart> findCartByCartId(String cartId) {
        return cartRepository.findCartByCartId(cartId);
    }

    @Override
    public Cart save(Optional<Cart> existingCartInDatabase) {
        return cartRepository.save(existingCartInDatabase.get());
    }

    @Override
    public void deleteById(String cartId) {
        cartRepository.deleteById(cartId);
    }


    @Override
    public void deleteCart(Cart cartCheckout) {
        cartRepository.delete(cartCheckout);
    }




}
