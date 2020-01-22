package com.baniya.cart.microservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginalProductDTO {

    private String productId;
    private String productName;
    private String productDescription;
    private Map productAttribute;
    private Double productRating;
    private String productUsp;
    private String imageUrl;
    private String categoryId;


}
