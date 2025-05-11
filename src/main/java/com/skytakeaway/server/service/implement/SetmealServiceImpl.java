package com.skytakeaway.server.service.implement;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.exception.DeletionNotAllowedException;
import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.SetmealDTO;
import com.skytakeaway.pojo.dto.SetmealPageQueryDTO;
import com.skytakeaway.pojo.entity.DishItem;
import com.skytakeaway.pojo.entity.Setmeal;
import com.skytakeaway.pojo.vo.DishItemVO;
import com.skytakeaway.pojo.vo.SetmealVO;
import com.skytakeaway.server.mapper.SetmealDishMapper;
import com.skytakeaway.server.mapper.SetmealMapper;
import com.skytakeaway.server.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public List<SetmealVO> list(SetmealVO setmeal) {
        List<SetmealVO> setmealList = setmealMapper.list(setmeal);

        //get ids of setmeal
        List<Long> setmealIds = setmealList.stream().map(SetmealVO::getId).collect(Collectors.toList());

        List<DishItem> dishItemList = setmealDishMapper.getBySetmealIds(setmealIds);

        //distribute dish item according to setmealId
        setmealList.forEach(setmealVO -> {
            List<DishItem> dishItems = dishItemList.stream().filter(item -> item.getSetmealId().equals(setmealVO.getId())).collect(Collectors.toList());
            setmealVO.setDishItems(dishItems);
        });

        return setmealList;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {
        //insert set meal data into set meal table
        Setmeal setmeal= new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insertSetmeal(setmeal);

        //insert dish data into set meal and dish relation table
        List<DishItem> dishItems = setmealDTO.getDishItems();
        for (DishItem item : dishItems) {
            item.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.insertBatch(dishItems);
    }

    //This is for admin client
    @Override
    public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public void changeStatus(Integer status, Long id) {
        setmealMapper.updateStatusById(status, id);
    }

    @Override
    @Transactional
    public void batchDeletion(List<Long> ids) {
        //determine set meal status
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //delete set meal from setmeal_table
        setmealMapper.batchDeletion(ids);

        //delete set meal relation from setmeal_dish_table
        setmealDishMapper.batchDeletion(ids);
    }

    @Override
    public SetmealVO getWithDishItemById(Long id) {
        //get set meal by id
        Setmeal setmeal = setmealMapper.getById(id);

        //get dish item by set meal id
        List<DishItem> dishItemList = setmealDishMapper.getBySetmealId(id);

        //prepare data
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setDishItems(dishItemList);

        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //update set meal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //delete old set meal dish relation
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //insert new set meal dish relation
        List<DishItem> dishItems = setmealDTO.getDishItems();
        dishItems.forEach(dishItem -> dishItem.setSetmealId(setmealDTO.getId()));
        setmealDishMapper.insertBatch(dishItems);
    }
}
