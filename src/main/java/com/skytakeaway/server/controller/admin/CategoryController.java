package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.CategoryDTO;
import com.skytakeaway.pojo.dto.CategoryPageQueryDTO;
import com.skytakeaway.pojo.entity.Category;
import com.skytakeaway.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Interface")
@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PutMapping
    @Operation(summary = "modify category")
    public Result<Void> updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "category page/name/type query")
    @Parameters({
            @Parameter(name = "name",description = "name",in = ParameterIn.QUERY),
            @Parameter(name = "pageNumber",description = "page number",required = true,in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "page size",required = true,in=ParameterIn.QUERY),
            @Parameter(name = "type",description = "type",in = ParameterIn.QUERY)
    })
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    @PostMapping("/status/{status}")
    @Operation(summary = "activate or block use of category")
    public Result<Void> modifyCategoryStatus(@PathVariable("status") Integer status,@RequestParam("id") Long id){
        categoryService.modifyCategoryStatus(status, id);
        return Result.success();
    }

    @PostMapping
    @Operation(summary = "add new category")
    public Result<Void> addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "delete category by id")
    public Result<Void> deleteCategory(@RequestParam("id") Long id){
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "get category of type")
    public Result<List<Category>> selectByType(Integer type){
        List<Category> categoryList = categoryService.selectByType(type);
        return Result.success(categoryList);
    }
}
