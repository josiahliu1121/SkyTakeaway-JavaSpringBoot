package com.skytakeaway.pojo.vo;

import com.skytakeaway.pojo.entity.OrderDetail;
import com.skytakeaway.pojo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrdersVo {
    private Long id;
    private String orderNumber;
    private Integer status;
    private LocalDateTime checkoutTime;
    private BigDecimal amount;
    private Integer deliveryStatus; //0 is not yet, 1 is done, this is for user use
    private List<OrderDetail> orderItems;
}
