package com.baniya.cart.microservice.dto;

import com.baniya.cart.microservice.entity.Cart;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;


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
    private Date timestamp;

    @Override
    public String toString() {
        return "OrderHistoryDTO{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", cart=" + cart +
                '}';

    }
}
