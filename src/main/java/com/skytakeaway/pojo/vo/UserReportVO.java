package com.skytakeaway.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportVO implements Serializable {
    //example: 2022-10-01, 2022-10-02, 2022-10-03
    private String dateList;

    //example:1,2,3,4
    private String totalUserList;

    //example:1,2,3,4
    private String newUserList;
}
