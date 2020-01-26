package com.baniya.cart.microservice.controller.impl;

import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="merchant",url="http://172.16.20.113:8081")
public interface MerchantProxy {


    @PostMapping("merchant/updateStockAfterOrder")
    public CheckoutCartDTO updateInventory(@RequestBody Cart cart);

    @GetMapping("merchant/viewProductByProductIdAndMerchantId/{productId}/{merchantId}")
    public MerchantProduct viewProductByProductIdAndMerchantId(@PathVariable("productId") String productId, @PathVariable("merchantId") String merchantId);
}
