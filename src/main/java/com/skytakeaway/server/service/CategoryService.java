package com.skytakeaway.server.service;

import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.CategoryDTO;
import com.skytakeaway.pojo.dto.CategoryPageQueryDTO;
import com.skytakeaway.pojo.entity.Category;

import java.util.List;

public interface CategoryService {
    void updateCategory(CategoryDTO categoryDTO);

    PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void modifyCategoryStatus(Integer status, Long id);

    void addCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    List<Category> selectByType(Integer type);

    List<Category> selectByStatus(Integer status);
}
