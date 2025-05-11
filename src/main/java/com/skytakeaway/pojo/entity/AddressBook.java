package com.skytakeaway.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook implements Serializable {
    private Long id;
    private Long userId;
    private String consignee;
    private String sex;
    private String phone;
    private String postcode;
    private String province;
    private String address;
    private String label;
    private Integer isDefault; //1 mean is default, 0 is not
}
