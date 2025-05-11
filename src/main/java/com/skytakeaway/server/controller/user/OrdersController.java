package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.OrdersSubmitDTO;
import com.skytakeaway.pojo.entity.OrderDetail;
import com.skytakeaway.pojo.entity.Orders;
import com.skytakeaway.pojo.vo.OrdersSubmitVO;
import com.skytakeaway.pojo.vo.UserOrdersVo;
import com.skytakeaway.server.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user orders interface")
@RestController("userOrdersController")
@RequestMapping("/user/order")
@Slf4j
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @PostMapping("/submit")
    @Operation(summary = "user submit order")
    public Result<OrdersSubmitVO> submitOrder (@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrdersSubmitVO ordersSubmitVO = ordersService.submitOrder(ordersSubmitDTO);
        return Result.success(ordersSubmitVO);
    }

    @GetMapping("/reminder/{id}")
    public Result<Void> reminder (@PathVariable("id") Long id){
        ordersService.reminder(id);
        return Result.success();
    }

    //this is just to stimulate user make payment
    @PostMapping("/pay/{orderNumber}")
    public Result<Void> makePayment (@PathVariable String orderNumber){
        ordersService.paySuccess(orderNumber);
        return Result.success();
    }

    @GetMapping("/getOrders/{deliveryStatus}")
    public Result<List<UserOrdersVo>> getOrders(@PathVariable Integer deliveryStatus){
        return Result.success(ordersService.getOrderForUser(deliveryStatus));
    }

    @PostMapping("/receive/{id}")
    public Result<Void> receiveOrder(@PathVariable Long id){
        ordersService.userReceiveOrder(id);
        return Result.success();
    }
}
