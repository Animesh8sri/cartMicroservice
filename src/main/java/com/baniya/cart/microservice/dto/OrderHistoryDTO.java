package com.baniya.cart.microservice.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class OrderHistoryDTO {

    @Id
    private String orderId;
    private String userId;
    private List<ProductDTO> products;

    @Override
    public String toString() {
        return
                "orderId='" + orderId + '\'' +
                ", products=" + products ;




    }
}
