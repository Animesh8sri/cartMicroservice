package com.baniya.cart.microservice.controller;


import com.baniya.cart.microservice.controller.impl.APIProxy;
import com.baniya.cart.microservice.controller.impl.CartProxy;
import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.service.CartService;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import netscape.javascript.JSObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class CartController {

    @Autowired
    CartService service;

    @Autowired
    APIProxy apiProxy;

    @Autowired
    CartProxy cartProxy;


    private static List<ProductDTO> checkout = new ArrayList<>();


    @GetMapping("/message")
    public String print(){
        System.out.println("message");
        return "hi";
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ResponseToCart> addToCart(@RequestBody AddToCarDTO addToCarDTO,@RequestHeader(required = false) HttpHeaders headers)
    {
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
        ProductDTO productDTO = cartProxy.viewProductById(addToCarDTO.getProductId());
        ResponseToCart responseToCart= new ResponseToCart();
        String productIdToBeAdded = addToCarDTO.getProductId();
        Optional<Cart> existingCartInDatabase = service.findCartByCartId(userProfile.getId());
        if(existingCartInDatabase.isPresent())
        {    Cart existingCart = existingCartInDatabase.get();

            if (null == existingCart.getProductDTO()) {
                 existingCart.setProductDTO(new ArrayList<>());
            }
            Optional<ProductDTO> productExist = existingCart.getProductDTO().stream().filter(product-> product.getProductId().equals(productIdToBeAdded)).findFirst();
            if(!productExist.isPresent())
            {
                productDTO.setCounter(1);
                existingCart.getProductDTO().add(productDTO);
                existingCart = service.save(existingCartInDatabase.get());
            }
            else
            {
                productExist.get().setCounter(productExist.get().getCounter() + 1);
                existingCart = service.save(existingCartInDatabase.get());
            }

            responseToCart.setCartId(existingCart.getCartId());
        }
        else {
            Cart newCart = new Cart();
            newCart.setCartId(userProfile.getId());
            productDTO.setCounter(1);
            newCart.setProductDTO(Collections.singletonList(productDTO));
            newCart = service.insert(newCart);
            responseToCart.setCartId(newCart.getCartId());
        }

        return new ResponseEntity<ResponseToCart>(responseToCart,HttpStatus.CREATED);
}

    @GetMapping("/viewCart")
    public ResponseEntity<Cart> viewCart(@RequestHeader(required = false) HttpHeaders headers)
    {

        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
        String cartId = userProfile.getId();
        Optional<Cart> cart  = service.findCartByCartId(cartId);
        if(cart.isPresent())
        {
            Double total = cart.get().getProductDTO().stream().map(productDTO -> productDTO.getCounter() * productDTO.getPrice()).mapToDouble(Double::doubleValue).sum();
            cart.get().setTotal(total);
            service.save(cart.get());

            return new ResponseEntity<Cart>(service.findCartByCartId(cartId).get(), HttpStatus.OK);

        }
        return null;


    }

    @PostMapping("/updateCart")
    public ResponseEntity<ResponseToCart> updateCart(@RequestBody UpdateCartDTO updateCartDTO, @RequestHeader HttpHeaders headers)
    {
        Cart cart = new Cart();
        String userIdHeader =  headers.get("Auth").get(0);
        UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
        String cartId = userProfile.getId();
        String productIdToBeUpdated;
        Integer countToBeUpdatted;

        productIdToBeUpdated = updateCartDTO.getProductId();
        countToBeUpdatted = updateCartDTO.getCounter();
        cart =service.findCartByCartId(cartId).get();
        Optional<ProductDTO> productUpdate = cart.getProductDTO().stream().filter(productDTO -> productDTO.getProductId().equals(productIdToBeUpdated)).findFirst();
        productUpdate.get().setCounter(countToBeUpdatted);
        Cart cartUpdated =new Cart();
        Double total = cart.getProductDTO().stream().map(productDTO -> productDTO.getCounter() * productDTO.getPrice()).mapToDouble(Double::doubleValue).sum();
        cart.setTotal(total);
        cartUpdated = service.save(cart);

        ResponseToCart responseToCart = new ResponseToCart();
        responseToCart.setCartId(cart.getCartId());
        responseToCart.setTotal(cart.getTotal());

        return new ResponseEntity<ResponseToCart>(responseToCart,HttpStatus.CREATED);


    }


    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@RequestHeader("authorization") String userIdHeader, @PathVariable("productId") String productId)
    {
        Cart cart = new Cart();
        UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
        String userId = userProfile.getId();
        cart = service.findCartByCartId(userId).get();
        List<ProductDTO> products = cart.getProductDTO();
        cart.setProductDTO(products.stream().filter(productDTO-> !productDTO.getProductId().equals(productId)).collect(Collectors.toList()));

        service.save(cart);
    }




}
