package com.skytakeaway.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressBookDTO implements Serializable {
    private String consignee;
    private String sex;
    private String phone;
    private String postcode;
    private String province;
    private String address;
    private String label;
    private Integer isDefault; //1 mean is default, 0 is not
}