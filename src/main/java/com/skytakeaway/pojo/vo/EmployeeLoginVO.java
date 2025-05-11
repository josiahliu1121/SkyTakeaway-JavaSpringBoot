package com.skytakeaway.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "Data return for employee login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginVO implements Serializable {
    private Long id;
    private String userName;
    private String name;
    @Schema(description = "jwt token generated", example = "eyJhbGciOiJIUzI1NiJ9.eyJlbXBJZCI6MSwiZXhwIjoxNzI4OTI2NzA0fQ.kex5f45GuZqv7ItTuvwQVxTCi3hOu4F3JA1wxvwOfhU")
    private String token;
}
