package com.skytakeaway.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {
    private String name;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer type;
}
