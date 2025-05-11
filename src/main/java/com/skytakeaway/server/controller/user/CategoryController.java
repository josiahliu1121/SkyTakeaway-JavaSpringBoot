package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.entity.Category;
import com.skytakeaway.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "user category interface")
@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "get category")
    public Result<List<Category>> selectByType(){
        List<Category> categoryList = categoryService.selectByStatus(StatusConstant.ENABLE);
        return Result.success(categoryList);
    }
}
