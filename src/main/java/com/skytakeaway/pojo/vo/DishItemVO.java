package com.skytakeaway.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {
    Integer copies;
    String description;
    String image;
    String name;
}
