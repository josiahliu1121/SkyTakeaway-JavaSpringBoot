package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.SetmealDTO;
import com.skytakeaway.pojo.dto.SetmealPageQueryDTO;
import com.skytakeaway.pojo.vo.SetmealVO;
import com.skytakeaway.server.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Setmeal Interface")
@Slf4j
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @PostMapping
    @Operation(summary = "add setmeal with dish")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<Void> addSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "setmeal page query")
    @Parameters({
            @Parameter(name = "name",description = "name",in = ParameterIn.QUERY),
            @Parameter(name = "page",description = "page number",required = true,in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "page size",required = true,in=ParameterIn.QUERY),
            @Parameter(name = "categoryId",description = "categoryId",in = ParameterIn.QUERY),
            @Parameter(name = "status",description = "1 = on sale, 0 =not on sale",in = ParameterIn.QUERY)
    })
    public Result<PageResult<SetmealVO>> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult<SetmealVO> result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "change setmeal status")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> changeStatus(@PathVariable Integer status, @RequestParam Long id){
        setmealService.changeStatus(status, id);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "setmeal batch deletion")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> batchDeletion(@RequestParam List<Long> ids){
        setmealService.batchDeletion(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get setmeal with dishes by id")
    public Result<SetmealVO> getWithDishById(@PathVariable Long id){
        return Result.success(setmealService.getWithDishItemById(id));
    }

    @PutMapping
    @Operation(summary = "update set meal data")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<Void> updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
}
