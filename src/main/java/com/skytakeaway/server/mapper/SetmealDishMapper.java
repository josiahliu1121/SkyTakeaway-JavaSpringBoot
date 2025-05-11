package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.entity.DishItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    public List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    void updateDishInformation(Dish dish);

    void insertBatch(List<DishItem> dishItems);

    void batchDeletion(List<Long> setmealIds);

    @Select("select * from setmeal_dish_table where setmeal_id = #{setmealId}")
    List<DishItem> getBySetmealId(Long setmealId);

    @Delete("delete from setmeal_dish_table where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    List<DishItem> getBySetmealIds(List<Long> setmealIds);
}
