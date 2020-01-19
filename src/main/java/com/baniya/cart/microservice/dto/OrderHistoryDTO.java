package com.baniya.cart.microservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class OrderHistoryDTO {

    private String orderId;
    private String merchantId;
    private String userEmailId;
    private String productName;
    private String timestamp;
    private String quantity;
    private String price;
}
