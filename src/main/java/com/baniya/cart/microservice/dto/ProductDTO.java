package com.baniya.cart.microservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private String productId;
    private String merchantId;
    private String productName;
    private String imageUrl;
    private Double price;
    private int stock;


}
