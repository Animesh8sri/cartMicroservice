package com.baniya.cart.microservice.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Document(collection = "Cart")
public class Cart implements Serializable {

    private String cartId;
    private String userEmailId;
    private String productId;
    private String merchantId;

    public Cart(){}

}
