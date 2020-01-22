package com.baniya.cart.microservice.controller.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="login",url = "http://172.16.20.82:8083")
public interface APIProxy {

    @GetMapping("/user/me")
    public Object login(@RequestHeader("Auth") String bearerToken);
}
