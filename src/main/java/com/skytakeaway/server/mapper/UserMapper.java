package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    Integer countByMap(Map map);

    @Select("select * from user_table where id = #{id}")
    User getById(Long id);

    @Select("select * from user_table where email_address = #{email}")
    User getByEmail(String email);

    void insertUser(User user);

    void updateUser(User user);
}
