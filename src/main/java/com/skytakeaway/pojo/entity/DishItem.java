package com.skytakeaway.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishItem implements Serializable {
    Long id;
    Long setmealId;
    Long dishId;
    BigDecimal price;
    Integer copies;
    String name;
}
