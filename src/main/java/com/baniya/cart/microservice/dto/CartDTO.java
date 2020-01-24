package com.baniya.cart.microservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class CartDTO implements Serializable {


    private String cartId;
    private ProductDTO productDTO;
}
