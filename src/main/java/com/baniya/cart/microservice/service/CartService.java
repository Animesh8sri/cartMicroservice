package com.baniya.cart.microservice.service;

import com.baniya.cart.microservice.dto.OriginalProductDTO;
import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.entity.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {

    //public ProductDTO viewProductById(String id);
    Cart insert(Cart cart);
    List<Cart> findAll();
    List<Cart> findByUser(String userId);
    public Cart findByUserAndProduct(String userId, String productId);
    public void deleteProduct(Cart productToBeDeleted);
    public boolean existsById(String cartId);
    public List<ProductDTO> viewProductsByProductIds(List<String> productIds);
    public void viewStockAndPrice(List<Cart> userIdList, Map<String, ProductDTO> productDTOMap);

    Cart findCartByCartId(String cartId);

    Cart save(Cart existingCartInDatabase);

}
