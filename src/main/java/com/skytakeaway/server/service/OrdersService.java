package com.skytakeaway.server.service;

import com.skytakeaway.pojo.dto.OrdersPageQueryDTO;
import com.skytakeaway.pojo.dto.OrdersSubmitDTO;
import com.skytakeaway.pojo.entity.OrderDetail;
import com.skytakeaway.pojo.vo.OrdersSubmitVO;
import com.skytakeaway.pojo.vo.OrdersVo;
import com.skytakeaway.pojo.vo.UserOrdersVo;

import java.util.List;

public interface OrdersService {
    OrdersSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    public void paySuccess(String orderNumber);

    void reminder(Long id);

    OrdersVo pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    void updateStatusById(Integer status, Long id);

    List<OrderDetail> getOrderDetail(Long id);

    List<UserOrdersVo> getOrderForUser(Integer deliveryStatus);

    void userReceiveOrder(Long id);
}
