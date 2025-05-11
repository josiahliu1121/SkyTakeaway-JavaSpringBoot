package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.OrdersPageQueryDTO;
import com.skytakeaway.pojo.dto.OrdersSubmitDTO;
import com.skytakeaway.pojo.entity.OrderDetail;
import com.skytakeaway.pojo.vo.OrdersSubmitVO;
import com.skytakeaway.pojo.vo.OrdersVo;
import com.skytakeaway.server.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "admin orders interface")
@RestController("adminOrdersController")
@RequestMapping("/admin/order")
@Slf4j
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @GetMapping("/page")
    @Operation(summary = "order page query")
    public Result<OrdersVo> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO){
        return Result.success(ordersService.pageQuery(ordersPageQueryDTO));
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "change status by id")
    public Result<Void> updateStatusById (@PathVariable Integer status, @RequestParam Long id){
        ordersService.updateStatusById(status, id);
        return Result.success();
    }

    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "get order detail by id")
    public Result<List<OrderDetail>> getOrderDetail(@PathVariable Long id){
        return Result.success(ordersService.getOrderDetail(id));
    }
}
