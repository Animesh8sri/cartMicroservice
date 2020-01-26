package com.baniya.cart.microservice.controller.impl;

import com.baniya.cart.microservice.dto.CartDTO;
import com.baniya.cart.microservice.dto.MerchantDTO;
import com.baniya.cart.microservice.dto.OriginalProductDTO;
import com.baniya.cart.microservice.dto.ProductDTO;
import com.baniya.cart.microservice.entity.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@FeignClient(name = "product", url = "http://172.16.20.51:8082")

public interface CartProxy {

    @GetMapping("/product/viewProductById/{productid}")
    ProductDTO viewProductById(@PathVariable("productid") String productid);

    @PostMapping("/product/productCart")
    List<ProductDTO> getCartProduct(@RequestBody List<String> ids);



}
