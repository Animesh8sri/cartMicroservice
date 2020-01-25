package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseToCart {

    Boolean isSuccess;
    String  errorMessage;
    String  cartId;
    Double total;

    public ResponseToCart(){
        isSuccess = true;
    }
}
