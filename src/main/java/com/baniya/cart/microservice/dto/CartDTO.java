package com.baniya.cart.microservice.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class CartDTO implements Serializable {


    private String userId;
    private String productId;
    private String merchantId;
    private int counter;
}
