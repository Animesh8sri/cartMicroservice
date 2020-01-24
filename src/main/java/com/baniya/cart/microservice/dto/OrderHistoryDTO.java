package com.baniya.cart.microservice.dto;

import com.baniya.cart.microservice.entity.Cart;
import lombok.*;
import org.springframework.data.annotation.Id;


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
    private Cart cart;

    @Override
    public String toString() {
        return "OrderHistoryDTO{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", cart=" + cart +
                '}';

    }
}
