package com.skytakeaway.server.mapper;

import com.github.pagehelper.Page;
import com.skytakeaway.common.enumeration.OperationType;
import com.skytakeaway.pojo.entity.Category;
import com.skytakeaway.server.annotation.AutoFill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    Page<Category> pageQuery(String name, Integer type);

    @Insert("insert into category_table(name, type, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{name}, #{type}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addCategory(Category category);

    @Select("select id, name, type, sort, status, create_time, update_time, create_user, update_user from category_table " +
            "where type=#{type}")
    List<Category> selectByType(Integer type);

    @Delete("delete from category_table where id=#{id}")
    void deleteCategoryById(Long id);

    @Select("select id, name, type, sort, status, create_time, update_time, create_user, update_user from category_table " +
            "where status = #{status}")
    List<Category> selectByStatus(Integer status);

    @Select("select * from category_table where id = #{id}")
    Category selectById(Long id);
}
