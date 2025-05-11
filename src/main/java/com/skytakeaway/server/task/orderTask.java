package com.skytakeaway.server.task;

import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.pojo.entity.Orders;
import com.skytakeaway.server.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class orderTask {

    @Autowired
    OrdersMapper ordersMapper;

    //execute every minutes
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeOutOrder(){
        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, orderTime);

        if (ordersList != null && ordersList.size() > 0){
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason(MessageConstant.ORDER_TIME_OUT);

                ordersMapper.update(orders);
            });
        }
    }

    //execute every day 1am
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder(){
        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-60); //process yesterday order
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTime(Orders.UNDER_DELIVERY, orderTime);

        if (ordersList != null && ordersList.size() > 0){
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETE);

                ordersMapper.update(orders);
            });
        }
    }
}
