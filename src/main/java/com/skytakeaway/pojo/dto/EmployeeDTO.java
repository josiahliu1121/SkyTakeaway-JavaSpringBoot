package com.skytakeaway.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String icNumber;
    private String phone;
    private String sex;
}
