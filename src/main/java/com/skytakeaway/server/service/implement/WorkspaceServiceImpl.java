package com.skytakeaway.server.service.implement;

import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.pojo.dto.DishSetmealOverviewDto;
import com.skytakeaway.pojo.entity.Orders;
import com.skytakeaway.pojo.vo.DailyBusinessDataVO;
import com.skytakeaway.pojo.vo.OrdersOverviewDTO;
import com.skytakeaway.server.mapper.DishMapper;
import com.skytakeaway.server.mapper.OrdersMapper;
import com.skytakeaway.server.mapper.SetmealMapper;
import com.skytakeaway.server.mapper.UserMapper;
import com.skytakeaway.server.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public DailyBusinessDataVO getDailyBusinessData() {
        //calculate the begin and end of today
        LocalDateTime begin = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return getBusinessData(begin, end);
    }

    @Override
    public DailyBusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        //calculate new user count
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        Integer newUserCount = userMapper.countByMap(map);

        //calculate order completion rate of today
        Integer totalOrderCount = ordersMapper.countByMap(map);
        map.put("status", Orders.COMPLETE);
        Integer validOrderCount = ordersMapper.countByMap(map);
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate =(validOrderCount.doubleValue() / totalOrderCount) * 100;
        }

        //calculate turnover
        Double turnover = ordersMapper.sumByMap(map);
        turnover = (turnover == null) ? 0.0 : turnover;

        //calculate unit price
        Double unitPrice = 0.0;
        if (validOrderCount != 0){
            unitPrice = turnover / validOrderCount;
        }

        return DailyBusinessDataVO.builder()
                .newUserCount(newUserCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .turnOver(turnover)
                .unitPrice(unitPrice)
                .build();
    }

    @Override
    public OrdersOverviewDTO getOrdersOverview() {
        //calculate the begin and end of today
        LocalDateTime begin = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        Integer totalOrders = ordersMapper.countByMap(map);

        map.put("status", Orders.CANCELED);
        Integer cancelledOrders = ordersMapper.countByMap(map);

        map.put("status", Orders.COMPLETE);
        Integer completedOrders = ordersMapper.countByMap(map);

        map.put("status", Orders.UNDER_DELIVERY);
        Integer deliveredOrders = ordersMapper.countByMap(map);

        map.put("status", Orders.PENDING_RECEIVE);
        Integer waitingOrders = ordersMapper.countByMap(map);

        return OrdersOverviewDTO.builder()
                .totalOrders(totalOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    @Override
    public DishSetmealOverviewDto getDishesOverview() {
        return DishSetmealOverviewDto.builder()
                .availableCount(dishMapper.getDishCountByStatus(StatusConstant.ENABLE))
                .disableCount(dishMapper.getDishCountByStatus(StatusConstant.DISABLE))
                .build();
    }

    @Override
    public DishSetmealOverviewDto getsetmealsOverview() {
        return DishSetmealOverviewDto.builder()
                .availableCount(setmealMapper.getSetmealCountByStatus(StatusConstant.ENABLE))
                .disableCount(setmealMapper.getSetmealCountByStatus(StatusConstant.DISABLE))
                .build();
    }


}
