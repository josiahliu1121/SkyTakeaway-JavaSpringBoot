package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.Flavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlavorMapper {
    void insertBatch(List<Flavor> flavors);

    void deleteByDishIds(List<Long> dishIds);

    @Delete("delete from flavor_table where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    @Select("select id, dish_id, name, value from flavor_table where dish_id = #{dishId}")
    List<Flavor> getByDishId(Long dishId);
}
