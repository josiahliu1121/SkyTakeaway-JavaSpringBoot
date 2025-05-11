package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.vo.DishVO;
import com.skytakeaway.server.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User Dish Interface")
@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    //@Autowired
    //private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @Operation(summary = "get dishes by category id")
    @Cacheable(cacheNames = "dishCache", key = "#categoryId")
    public Result<List<DishVO>> list(Long categoryId){
        /*
        //construct redis key
        String key = "dish_" + categoryId;

        //request data from redis server
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0){
            return Result.success(list);
        }
         */
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        List<DishVO> list = dishService.listWithFlavor(dish);

        //sava data into redis if not exists
        //redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }
}
