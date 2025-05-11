package com.skytakeaway.server.service.implement;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.exception.DeletionNotAllowedException;
import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.CategoryDTO;
import com.skytakeaway.pojo.dto.CategoryPageQueryDTO;
import com.skytakeaway.pojo.entity.Category;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.vo.SetmealVO;
import com.skytakeaway.server.mapper.CategoryMapper;
import com.skytakeaway.server.mapper.DishMapper;
import com.skytakeaway.server.mapper.SetmealMapper;
import com.skytakeaway.server.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.updateCategory(category);
    }

    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // PageHelper will handle pagination
        PageHelper.startPage(categoryPageQueryDTO.getPageNumber(), categoryPageQueryDTO.getPageSize());

        // Pass both name and type to the mapper
        Page<Category> result = categoryMapper.pageQuery(
                categoryPageQueryDTO.getName(),
                categoryPageQueryDTO.getType()
        );

        return new PageResult<>(result.getTotal(), result.getResult());
    }

    @Override
    public void modifyCategoryStatus(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();

        categoryMapper.updateCategory(category);
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.addCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {
        //determine if there is set meal or dish under this category
        Dish dish = new Dish();
        dish.setCategoryId(id);
        SetmealVO setmeal = new SetmealVO();
        setmeal.setCategoryId(id);
        if(!dishMapper.list(dish).isEmpty() || !setmealMapper.list(setmeal).isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_DELETE_NOT_ALLOW);
        }

        //determine the category status
        Category category = categoryMapper.selectById(id);
        if(category.getStatus() == StatusConstant.ENABLE){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_IS_IN_USED);
        }

        //delete the category
        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public List<Category> selectByType(Integer type) {
        return categoryMapper.selectByType(type);
    }

    @Override
    public List<Category> selectByStatus(Integer status) {
        return categoryMapper.selectByStatus(status);
    }

}
