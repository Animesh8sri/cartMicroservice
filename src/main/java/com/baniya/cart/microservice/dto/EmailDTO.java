package com.baniya.cart.microservice.dto;

import com.baniya.cart.microservice.entity.OrderHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    private String userEmailId;
    private OrderHistoryDTO orderHistoryDTO;
}
