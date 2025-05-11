package com.skytakeaway.server.service;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.SetmealDTO;
import com.skytakeaway.pojo.dto.SetmealPageQueryDTO;
import com.skytakeaway.pojo.entity.Setmeal;
import com.skytakeaway.pojo.vo.DishItemVO;
import com.skytakeaway.pojo.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    List<SetmealVO> list(SetmealVO setmeal);

    List<DishItemVO> getDishItemById(Long id);

    void addSetmeal(SetmealDTO setmealDTO);

    PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void changeStatus(Integer status, Long id);

    void batchDeletion(List<Long> ids);

    SetmealVO getWithDishItemById(Long id);

    void updateSetmeal(SetmealDTO setmealDTO);
}
