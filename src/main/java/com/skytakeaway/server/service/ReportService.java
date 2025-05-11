package com.skytakeaway.server.service;

import com.skytakeaway.pojo.vo.OrderReportVO;
import com.skytakeaway.pojo.vo.SalesTop10ReportVO;
import com.skytakeaway.pojo.vo.TurnoverReportVO;
import com.skytakeaway.pojo.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnoverStatistic(LocalDate begin, LocalDate end);

    UserReportVO getUserStatistic(LocalDate begin, LocalDate end);

    OrderReportVO getOrderStatistic(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
