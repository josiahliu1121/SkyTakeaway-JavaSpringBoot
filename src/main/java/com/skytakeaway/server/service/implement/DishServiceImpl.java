package com.skytakeaway.server.service.implement;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.exception.DeletionNotAllowedException;
import com.skytakeaway.common.exception.FlavorNameIsNull;
import com.skytakeaway.common.exception.FlavorValueIsNullException;
import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.DishDTO;
import com.skytakeaway.pojo.dto.DishPageQueryDTO;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.entity.Flavor;
import com.skytakeaway.pojo.vo.DishVO;
import com.skytakeaway.server.mapper.DishMapper;
import com.skytakeaway.server.mapper.FlavorMapper;
import com.skytakeaway.server.mapper.SetmealDishMapper;
import com.skytakeaway.server.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    FlavorMapper flavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish =new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //insert one data into dish table
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        //insert multiple data into flavor table
        List<Flavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> {
                if(flavor.getValue().isEmpty()){
                    throw new FlavorValueIsNullException(MessageConstant.FLAVOR_VALUE_IS_NULL);
                }

                if(flavor.getName() == null | flavor.getName() == ""){
                    throw new FlavorNameIsNull(MessageConstant.FLAVOR_NAME_IS_NULL);
                }

                flavor.setDishId(dishId);
            });
            flavorMapper.insertBatch(flavors);
        }
    }

    //This is for admin client
    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //determine if the dish is on sale
        for(Long id :ids){
            Dish dish = dishMapper.getDishById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //determine if set meal is linked to the dish
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //delete dish with its flavor from database
        dishMapper.deleteByIds(ids);
        flavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //get dish by id
        Dish dish  = dishMapper.getDishById(id);

        //get flavor with id
        List<Flavor> flavors = flavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //update dish data
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //delete original flavor data
        flavorMapper.deleteByDishId(dishDTO.getId());

        //insert new flavor data
        List<Flavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(flavor -> {
                if(flavor.getValue().isEmpty()){
                    throw new FlavorValueIsNullException(MessageConstant.FLAVOR_VALUE_IS_NULL);
                }

                if(flavor.getName() == null | flavor.getName() == ""){
                    throw new FlavorNameIsNull(MessageConstant.FLAVOR_NAME_IS_NULL);
                }

                flavor.setDishId(dishDTO.getId());
            });
            flavorMapper.insertBatch(flavors);
        }

        //update dish data in set meal dish relation table
        setmealDishMapper.updateDishInformation(dish);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            List<Flavor> flavorList = flavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavorList);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        return dishMapper.list(dish);
    }

    @Override
    public void changeStatusById(Integer status, Long id) {
        dishMapper.changeStatusById(status, id);
    }
}
