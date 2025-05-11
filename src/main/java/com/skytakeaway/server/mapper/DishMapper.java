package com.skytakeaway.server.mapper;

import com.github.pagehelper.Page;
import com.skytakeaway.common.enumeration.OperationType;
import com.skytakeaway.pojo.dto.DishPageQueryDTO;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.vo.DishVO;
import com.skytakeaway.server.annotation.AutoFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {
    //check number of dish under certain category
    @Select("select count(id) from dish_table where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user from dish_table where id = #{id}")
    Dish getDishById (Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> list(Dish dish);

    @Select("select count(id) from dish_table where status = #{status}")
    Integer getDishCountByStatus (Integer status);

    @Update("update dish_table set status = #{status} where id = #{id}")
    void changeStatusById(Integer status, Long id);
}
