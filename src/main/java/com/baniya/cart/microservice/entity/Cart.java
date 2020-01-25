package com.baniya.cart.microservice.entity;


import com.baniya.cart.microservice.dto.ProductDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Document(collection = "Cart")
public class Cart implements Serializable {

    @Id
    private String cartId;
    private List<ProductDTO> productDTO;
    private double total;

    public Cart(){}

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", productDTO=" + productDTO +
                '}';
    }
}
