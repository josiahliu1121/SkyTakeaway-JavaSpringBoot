package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetails);

    @Select("select * from order_detail_table where order_id = #{orderId}")
    List<OrderDetail> getById(Long orderId);

    List<OrderDetail> getByIds(List<Long> ordersIds);
}
