package com.baniya.cart.microservice.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Document(collection = "Cart")
public class Cart implements Serializable {


    @Id
    private String id;
    private String userId;
    private String productId;
    private String merchantId;
    private int counter;

    public Cart(){}

}
