package com.skytakeaway.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersOverviewDTO implements Serializable {
    private Integer totalOrders;
    private Integer cancelledOrders;
    private Integer completedOrders;
    private Integer deliveredOrders;
    private Integer waitingOrders;
}
