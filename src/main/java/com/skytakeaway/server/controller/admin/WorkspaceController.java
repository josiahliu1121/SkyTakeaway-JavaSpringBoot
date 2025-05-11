package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.DishSetmealOverviewDto;
import com.skytakeaway.pojo.vo.DailyBusinessDataVO;
import com.skytakeaway.pojo.vo.OrdersOverviewDTO;
import com.skytakeaway.server.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "admin workspace interface")
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    @GetMapping("/businessData")
    @Operation(summary = "daily business data")
    public Result<DailyBusinessDataVO> dailyBusinessData (){
        return Result.success(workspaceService.getDailyBusinessData());
    }

    @GetMapping("/ordersOverview")
    @Operation(summary = "orders overview")
    public Result<OrdersOverviewDTO> ordersOverview (){
        return Result.success(workspaceService.getOrdersOverview());
    }

    @GetMapping("/dishesOverview")
    @Operation(summary = "dishes overview")
    public Result<DishSetmealOverviewDto> dishesOverview (){
        return Result.success(workspaceService.getDishesOverview());
    }

    @GetMapping("/setmealsOverview")
    @Operation(summary = "setmeals overview")
    public Result<DishSetmealOverviewDto> setmealsOverview (){
        return Result.success(workspaceService.getsetmealsOverview());
    }
}
