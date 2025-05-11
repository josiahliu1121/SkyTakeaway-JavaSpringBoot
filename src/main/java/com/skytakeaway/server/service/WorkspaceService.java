package com.skytakeaway.server.service;

import com.skytakeaway.pojo.dto.DishSetmealOverviewDto;
import com.skytakeaway.pojo.vo.DailyBusinessDataVO;
import com.skytakeaway.pojo.vo.OrdersOverviewDTO;

import java.time.LocalDateTime;

public interface WorkspaceService {
    DailyBusinessDataVO getDailyBusinessData();

    DailyBusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrdersOverviewDTO getOrdersOverview();

    DishSetmealOverviewDto getDishesOverview();

    DishSetmealOverviewDto getsetmealsOverview();
}
