package com.skytakeaway.server.mapper;

import com.github.pagehelper.Page;
import com.skytakeaway.common.enumeration.OperationType;
import com.skytakeaway.pojo.entity.Employee;
import com.skytakeaway.server.annotation.AutoFill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    @Select("select id, username, password, name, ic_number, phone, sex, status, create_time, update_time, create_user, update_user from employee_table " +
            "where username=#{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee_table(username, password, name, ic_number, phone, sex, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{username}, #{password}, #{name}, #{icNumber}, #{phone}, #{sex}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addEmployee(Employee employee);

    Page<Employee> pageQuery(String name);

    @AutoFill(value = OperationType.UPDATE)
    void updateEmployee(Employee employee);

    @Select("select id, username, password, name, ic_number, phone, sex, status, create_time, update_time, create_user, update_user from employee_table " +
            "where id=#{id}")
    Employee getById(Long id);

    @Delete("delete from employee_table where id = #{id}")
    void deleteEmployeeById(Long id);
}
