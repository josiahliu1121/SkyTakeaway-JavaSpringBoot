package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.DishDTO;
import com.skytakeaway.pojo.dto.DishPageQueryDTO;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.vo.DishVO;
import com.skytakeaway.server.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Admin Dish Interface")
@Slf4j
@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @PostMapping
    @Operation(summary = "add dish with flavor")
    @CacheEvict(cacheNames = "dishCache", key = "#dishDTO.categoryId")
    public Result<Void> addDishWithFlavor(@RequestBody DishDTO dishDTO){
        dishService.addDishWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "dish page query")
    @Parameters({
            @Parameter(name = "name",description = "name",in = ParameterIn.QUERY),
            @Parameter(name = "page",description = "page number",required = true,in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "page size",required = true,in=ParameterIn.QUERY),
            @Parameter(name = "categoryId",description = "categoryId",in = ParameterIn.QUERY),
            @Parameter(name = "status",description = "1 = on sale, 0 =not on sale",in = ParameterIn.QUERY)
    })
    public Result<PageResult<DishVO>> pageQuery (DishPageQueryDTO dishPageQueryDTO){
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return  Result.success(pageResult);
    }

    @DeleteMapping
    @Operation(summary = "delete dishes")
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    // RequestParam add to convert string to list (example: {1,2,3})
    public Result<Void> deleteDishes(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        return Result.success();
    };

    @GetMapping("/{id}")
    @Operation(summary = "get dish by id with flavor")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id){
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @Operation(summary = "update dish with flavor")
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public Result<Void> updateWithFlavor (@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "get dish by category")
    public Result<List<Dish>> getByCategoryId (@RequestParam Long categoryId){
        return Result.success(dishService.getByCategoryId(categoryId));
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "change status by id")
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public Result<Void> changeStatusById(@PathVariable Integer status, @RequestParam Long id){
        dishService.changeStatusById(status, id);
        return Result.success();
    }
}
