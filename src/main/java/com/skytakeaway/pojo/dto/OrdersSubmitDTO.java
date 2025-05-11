package com.skytakeaway.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {
    private Long addressBookId;
    private Integer payMethod;
    private BigDecimal amount;
    private String remark;
    private LocalDateTime estimatedDeliveryTime;
    private Integer deliveryStatus;
    private Integer packAmount;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
}
