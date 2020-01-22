package com.baniya.cart.microservice.service.impl;


import com.baniya.cart.microservice.controller.impl.CartProxy;
import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.dto.MerchantDTO;
import com.baniya.cart.microservice.dto.OriginalProductDTO;
import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.repository.CartRepository;
import com.baniya.cart.microservice.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
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




      //  @Override
    //   public ProductDTO viewProductById(String id) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ProductDTO productDTO  = new ProductDTO();
//        OriginalProductDTO product = cartProxy.viewProductById(id);
//        return productDTO;
//    }

    @Override
    public Cart insert(Cart cart) {
        return cartRepository.insert(cart);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public List<Cart> findByUser(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart findByUserAndProduct(String userId, String productId) {
        Optional<Cart> cart = cartRepository.findById(userId + "_" + productId);
        return cart.orElse(null);
    }

    @Override
    public void deleteProduct(Cart productToBeDeleted) {
        cartRepository.delete(productToBeDeleted);
    }

    @Override
    public boolean existsById(String cartId) {
        return cartRepository.existsById(cartId);
    }

    @Override
    public List<ProductDTO> viewProductsByProductIds(List<String> productIds) {

        List<ProductDTO> product = cartProxy.getCartProduct(productIds);

        return product;
    }

    @Override
    public void viewStockAndPrice(List<Cart> userIdList, Map<String, ProductDTO> productDTOMap) {
        Map<String,MerchantDTO> stockAndPrice = new HashMap<>();
        Cart cart=null;
        stockAndPrice = merchantProxy.viewPriceAndStockByProductId(userIdList);
        for(Cart user:userIdList) {
            try {
                productDTOMap.get(user.getProductId()).setPrice(stockAndPrice.get(user.getProductId() + "_" + user.getMerchantId()).getPrice());
                productDTOMap.get(user.getProductId()).setStock(stockAndPrice.get(user.getProductId() + "_" + user.getMerchantId()).getStock());
                productDTOMap.get(user.getProductId()).setCounter(user.getCounter());
                productDTOMap.get(user.getProductId()).setMerchantId(stockAndPrice.get(user.getProductId() + "_" + user.getMerchantId()).getMerchantDetails().getMerchantId());

            } catch (Exception ex) {

            }
            }

    }

    @Override
    public Cart findCartByCartId(String cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()){
            return cartOptional.get();
        }
        return null;
    }


    public Cart save(Cart existingCartInDatabase) {
        return cartRepository.save(existingCartInDatabase);
    }
}
