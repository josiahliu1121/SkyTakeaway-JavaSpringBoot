package com.skytakeaway.server.mapper;

import com.github.pagehelper.Page;
import com.skytakeaway.pojo.dto.GoodSalesDTO;
import com.skytakeaway.pojo.dto.OrdersPageQueryDTO;
import com.skytakeaway.pojo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    void insert(Orders orders);

    void update(Orders orders);

    @Select("select * from orders_table where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime (Integer status, LocalDateTime orderTime);

    @Select("select * from orders_table where order_number = #{orderNumber} and user_id = #{userId}")
    Orders getByOrderNumberAndUserId(String orderNumber, Long userId);

    @Select("select * from orders_table where id = #{id}")
    Orders getById(Long id);

    Double sumByMap(Map map);

    Integer countByMap(Map map);

    //get top 10 sales for both dish and set meal
    List<GoodSalesDTO> getSalesTop10 (LocalDateTime begin, LocalDateTime end);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders_table where user_id = #{userId} and delivery_status = #{deliveryStatus}")
    List<Orders> getByUserIdAndDeliveryStatus(Long userId, Integer deliveryStatus);
}
