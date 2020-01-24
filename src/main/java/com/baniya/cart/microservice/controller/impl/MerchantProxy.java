package com.baniya.cart.microservice.controller.impl;

import com.baniya.cart.microservice.dto.CartDTO;
import com.baniya.cart.microservice.dto.CheckoutCartDTO;
import com.baniya.cart.microservice.dto.MerchantDTO;
import com.baniya.cart.microservice.dto.UpdateCartDTO;
import com.baniya.cart.microservice.entity.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name="merchant",url="http://172.16.20.113:8081")
public interface MerchantProxy {

    @PostMapping("/productMerchant")
    public Map<String, MerchantDTO> viewPriceAndStockByProductId(@RequestBody List<Cart> cartDTO);

    @PostMapping("/updateStockAfterOrder")
    public CheckoutCartDTO updateInventory(@RequestBody Cart cart);
}
