package com.baniya.cart.microservice.entity;


import com.baniya.cart.microservice.dto.ProductDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Document(collection = "OrderHistory")
public class OrderHistory implements Serializable {


    @Id
    private String orderId;
    private String userId;
    private Cart cart;
    private Date timestamp;

    @Override
    public String toString() {
        return "OrderHistory{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", cart=" + cart +
                '}';
    }

    public OrderHistory(){}

}
