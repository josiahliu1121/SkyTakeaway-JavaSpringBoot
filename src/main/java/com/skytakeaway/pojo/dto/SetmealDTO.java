package com.skytakeaway.pojo.dto;

import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.entity.DishItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDTO implements Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer status;
    private List<DishItem> dishItems = new ArrayList<>();
}
