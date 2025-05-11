package com.skytakeaway.server.service.implement;

import com.skytakeaway.pojo.dto.GoodSalesDTO;
import com.skytakeaway.pojo.entity.Orders;
import com.skytakeaway.pojo.vo.*;
import com.skytakeaway.server.mapper.OrdersMapper;
import com.skytakeaway.server.mapper.UserMapper;
import com.skytakeaway.server.service.ReportService;
import com.skytakeaway.server.service.WorkspaceService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO getTurnoverStatistic(LocalDate begin, LocalDate end) {
        //produce date data, from begin to end
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //produce data according to date
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETE);
            Double turnover = ordersMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistic(LocalDate begin, LocalDate end) {
        //produce date data, from begin to end
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //produce data according to date
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            Integer totalUserCount = userMapper.countByMap(map);
            totalUserList.add(totalUserCount);

            map.put("end", endTime);
            Integer newUserCount = userMapper.countByMap(map);
            newUserList.add(newUserCount);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistic(LocalDate begin, LocalDate end) {
        //produce date data, from begin to end
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //produce data according to date
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            orderCountList.add(ordersMapper.countByMap(map));

            map.put("status", Orders.COMPLETE);
            validOrderCountList.add(ordersMapper.countByMap(map));
        }

        //calculate total
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer totalValidOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate = totalValidOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .totalValidOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodSalesDTO> salesTop10 = ordersMapper.getSalesTop10(beginTime, endTime);

        List<String> nameList = salesTop10.stream().map(GoodSalesDTO::getName).collect(Collectors.toList());
        List<String> numberList = salesTop10.stream().map(GoodSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //read data from database
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        DailyBusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));

        //read the excel template through POI
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/Report Template.xlsx");

        try {
            //create a new excel file base on template
            XSSFWorkbook excelTemplate = new XSSFWorkbook(inputStream);

            //get the sheet to edit
            XSSFSheet sheet = excelTemplate.getSheetAt(0);

            //write data into the sheet
            sheet.getRow(1).getCell(0).setCellValue("Time: " + begin + " to " + end);

            XSSFRow row = sheet.getRow(3);
            row.getCell(1).setCellValue("RM" + businessData.getTurnOver());
            row.getCell(3).setCellValue(businessData.getOrderCompletionRate() * 100 + "%");
            row.getCell(5).setCellValue(businessData.getNewUserCount());

            row = sheet.getRow(4);
            row.getCell(1).setCellValue(businessData.getValidOrderCount());
            row.getCell(3).setCellValue("RM" + businessData.getUnitPrice());

            for (int i = 0; i < 30; i++) {
                //write and read daily data
                LocalDate date = begin.plusDays(i);
                DailyBusinessDataVO dailyBusinessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                row = sheet.getRow(7 + i);
                row.getCell(0).setCellValue(date.toString());
                row.getCell(1).setCellValue("RM" + dailyBusinessData.getTurnOver());
                row.getCell(2).setCellValue(dailyBusinessData.getValidOrderCount());
                row.getCell(3).setCellValue(dailyBusinessData.getOrderCompletionRate() * 100 + "%");
                row.getCell(4).setCellValue("RM" + dailyBusinessData.getUnitPrice());
                row.getCell(5).setCellValue(dailyBusinessData.getNewUserCount());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"BusinessDataReport.xlsx\"");

            //download the generated file for client through output stream
            ServletOutputStream outputStream = response.getOutputStream();
            excelTemplate.write(outputStream);

            outputStream.close();
            excelTemplate.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
