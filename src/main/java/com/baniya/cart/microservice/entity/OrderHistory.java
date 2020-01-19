package com.baniya.cart.microservice.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Document(collection = "OrderHistory")
public class OrderHistory implements Serializable {

    private String orderId;
    private String merchantId;
    private String userEmailId;
    private String productName;
    private String timestamp;
    private String quantity;
    private String price;

    public OrderHistory(){}

}
