package com.skytakeaway.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyBusinessDataVO implements Serializable {
    private Integer newUserCount;
    private Double orderCompletionRate;
    private Double turnOver;
    private Double unitPrice; //average spent of a customer
    private Integer validOrderCount;
}
