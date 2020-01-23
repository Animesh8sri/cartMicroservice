package com.baniya.cart.microservice.controller.impl;

import com.baniya.cart.microservice.dto.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="login",url = "http://172.16.20.82:8083")
public interface APIProxy {

    @GetMapping("authenticate/user/me")
    public UserProfile getCurrentUser(@RequestHeader("Auth") String bearerToken);
}
