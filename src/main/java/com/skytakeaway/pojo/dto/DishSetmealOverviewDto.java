package com.skytakeaway.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishSetmealOverviewDto implements Serializable {
    private Integer availableCount;
    private Integer disableCount;
}
