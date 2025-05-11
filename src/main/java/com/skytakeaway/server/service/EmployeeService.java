package com.skytakeaway.server.service;


import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.EmployeeDTO;
import com.skytakeaway.pojo.dto.EmployeeLoginDTO;
import com.skytakeaway.pojo.dto.EmployeePageQueryDTO;
import com.skytakeaway.pojo.entity.Employee;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void modifyEmployeeStatus(Integer status, Long id);

    Employee getEmployeeById (Long id);

    void updateEmployee (EmployeeDTO employeeDTO);

    void deleteEmployeeById (Long id);
}
