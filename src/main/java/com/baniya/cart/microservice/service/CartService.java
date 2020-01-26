package com.baniya.cart.microservice.service;

import com.baniya.cart.microservice.entity.Cart;

import java.util.Optional;

public interface CartService {

    //public ProductDTO viewProductById(String id);
    Cart insert(Cart cart);
//    List<Cart> findAll();
//    List<Cart> findByUser(String userId);
//    public Cart findByUserAndProduct(String userId, String productId);
//    public void deleteProduct(Cart productToBeDeleted);
//    public boolean existsById(String cartId);
//    public List<ProductDTO> viewProductsByProductIds(List<String> productIds);
//    public void viewStockAndPrice(List<Cart> userIdList, Map<String, ProductDTO> productDTOMap);

    Optional<Cart> findCartByCartId(String cartId);

    Cart save(Optional<Cart> existingCartInDatabase);
    public void deleteById(String cartId);


    void deleteCart(Cart cartCheckout);
}
