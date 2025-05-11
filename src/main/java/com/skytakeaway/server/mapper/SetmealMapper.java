package com.skytakeaway.server.mapper;

import com.github.pagehelper.Page;
import com.skytakeaway.common.enumeration.OperationType;
import com.skytakeaway.pojo.dto.SetmealPageQueryDTO;
import com.skytakeaway.pojo.entity.Setmeal;
import com.skytakeaway.pojo.vo.DishItemVO;
import com.skytakeaway.pojo.vo.SetmealVO;
import com.skytakeaway.server.annotation.AutoFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {
    List<SetmealVO> list(SetmealVO setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish_table sd " +
            "left join dish_table d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    @Select("select * from setmeal_table where id = #{id}")
    Setmeal getById(Long id);

    @Select("select count(id) from setmeal_table where status = #{status}")
    Integer getSetmealCountByStatus (Integer status);

    @AutoFill(OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);


    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @Update("update setmeal_table set status = #{status} where id = #{id}")
    void updateStatusById(Integer status, Long id);

    void batchDeletion(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
