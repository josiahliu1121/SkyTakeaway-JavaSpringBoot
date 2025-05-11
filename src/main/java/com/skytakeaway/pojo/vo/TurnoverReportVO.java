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
public class TurnoverReportVO implements Serializable {
    //example: 2022-10-01, 2022-10-02, 2022-10-03
    private String dateList;

    //example:404.1, 232.3, 234.3
    private String turnoverList;
}
