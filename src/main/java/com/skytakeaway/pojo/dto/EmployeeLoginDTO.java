package com.skytakeaway.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "Employee login information from user")
@Data
public class EmployeeLoginDTO implements Serializable {
    private String username;
    private String password;
}
