package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.entity.Setmeal;
import com.skytakeaway.pojo.vo.DishItemVO;
import com.skytakeaway.pojo.vo.SetmealVO;
import com.skytakeaway.server.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User Setmeael Interface")
@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @GetMapping("/list")
    @Operation(summary = "get set meals by category id")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<SetmealVO>> list (Long categoryId) {
        SetmealVO setmeal = new SetmealVO();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<SetmealVO> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @Operation(summary = "get dish by set meal id")
    public Result<List<DishItemVO>> dishList(@PathVariable(name = "id") Long id){
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
