package com.skytakeaway.pojo.vo;

import com.skytakeaway.pojo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersVo {
    private Long total;
    private Integer pendingCount;
    private Integer receiveCount;
    private Integer deliveryCount;
    private List<Orders> records;
}
