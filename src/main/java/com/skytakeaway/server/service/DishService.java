package com.skytakeaway.server.service;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.DishDTO;
import com.skytakeaway.pojo.dto.DishPageQueryDTO;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.vo.DishVO;

import java.util.List;

public interface DishService {
    public void addDishWithFlavor(DishDTO dishDTO);

    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dishDTO);

    List<DishVO> listWithFlavor(Dish dish);

    List<Dish> getByCategoryId(Long categoryId);

    void changeStatusById(Integer status, Long id);
}
