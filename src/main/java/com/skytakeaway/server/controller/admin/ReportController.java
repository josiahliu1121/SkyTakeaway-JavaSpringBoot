package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.vo.OrderReportVO;
import com.skytakeaway.pojo.vo.SalesTop10ReportVO;
import com.skytakeaway.pojo.vo.TurnoverReportVO;
import com.skytakeaway.pojo.vo.UserReportVO;
import com.skytakeaway.server.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "admin report interface")
@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/turnoverStatistic")
    @Operation(summary = "turnover statistic")
    public Result<TurnoverReportVO> turnoverStatistic (
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        return Result.success(reportService.getTurnoverStatistic(begin, end));
    }

    @GetMapping("/userStatistic")
    @Operation(summary = "user statistic")
    public Result<UserReportVO> userStatistic(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        return Result.success(reportService.getUserStatistic(begin, end));
    }

    @GetMapping("/orderStatistic")
    @Operation(summary = "order statistic")
    public Result<OrderReportVO> orderStatistic(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        return Result.success(reportService.getOrderStatistic(begin, end));
    }

    @GetMapping("/top10")
    @Operation(summary = "sales top 10")
    public Result<SalesTop10ReportVO> salesTop10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        return Result.success(reportService.getSalesTop10(begin, end));
    }

    @GetMapping("/export")
    @Operation(summary = "export excel format report")
    public void export (HttpServletResponse response){
        reportService.exportBusinessData(response);
    }

}
