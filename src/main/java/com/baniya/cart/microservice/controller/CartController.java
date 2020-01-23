package com.baniya.cart.microservice.controller;


import com.baniya.cart.microservice.controller.impl.APIProxy;
import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {

    @Autowired
    CartService service;

    @Autowired
    APIProxy apiProxy;


    private static List<ProductDTO> checkout = new ArrayList<>();

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody CartDTO cartDTO, @RequestHeader HttpHeaders headers)
    {
        int count;
        Cart cart = new Cart();
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);

        cartDTO.setUserId(userProfile.getId());

        BeanUtils.copyProperties(cartDTO,cart);
        String cartId = cartDTO.getUserId() + "_" + cartDTO.getProductId();

        Cart existingCartInDatabase = service.findCartByCartId(cartId);
        Cart cartCreated;
        if(Objects.nonNull(existingCartInDatabase))
        {
            count = existingCartInDatabase.getCounter() + 1;
            existingCartInDatabase.setCounter(count);
            cartCreated = service.save(existingCartInDatabase);
        }
        else {
            cart.setCounter(1);
            cart.setId(cartDTO.getUserId() + "_" + cartDTO.getProductId());
            cartCreated = service.insert(cart);
        }

        return new ResponseEntity<String>(cartCreated.getUserId(),HttpStatus.CREATED);
    }

    @GetMapping("/viewCart")
    public List<ProductDTO> viewProductById(@RequestHeader HttpHeaders headers){

        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userDTO = (UserProfile) apiProxy.getCurrentUser(userIdHeader);

        ProductDTO productDTO = new ProductDTO();
        List<Cart> userIdList = service.findByUser(userDTO.getId());
        List<String> productIds = userIdList.stream().map(Cart::getProductId).collect(Collectors.toList());
        List<ProductDTO> products = new ArrayList<>();
        products = service.viewProductsByProductIds(productIds);
        Map<String, ProductDTO> map = new HashMap<>();
        for(ProductDTO product: products){
            if (null != product.getProductId()) {
                map.put(product.getProductId(), product);
            }
        }

        service.viewStockAndPrice(userIdList, map);

        return products;
    }

    @PostMapping("/updateCart")
    public ResponseEntity<String> updateCart(@RequestBody CartDTO cartDTO, @RequestHeader HttpHeaders headers)
    {
        Cart cart = new Cart();
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userDTO = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
        cartDTO.setUserId(userDTO.getId());
        BeanUtils.copyProperties(cartDTO,cart);
        String cartId = cartDTO.getUserId() + "_" + cartDTO.getProductId();

        Cart existingCartInDatabase = service.findCartByCartId(cartId);
        Cart cartUpdated = null;

        if(Objects.nonNull(existingCartInDatabase))
        {
            existingCartInDatabase.setCounter(cartDTO.getCounter());
            cartUpdated = service.save(existingCartInDatabase);
        }

        return new ResponseEntity<String>(cartUpdated.getUserId(),HttpStatus.CREATED);


    }



    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@RequestHeader("authorization") String userIdHeader, @PathVariable("productId") String productId)
    {
        UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
        String userId = userProfile.getId();
        ProductDTO productDTO = new ProductDTO();
        Cart productToBeDeleted = service.findByUserAndProduct(userId,productId);
        service.deleteProduct(productToBeDeleted);
    }




}
