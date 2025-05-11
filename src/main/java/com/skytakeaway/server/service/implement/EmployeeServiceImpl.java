package com.skytakeaway.server.service.implement;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.constant.PasswordConstant;
import com.skytakeaway.common.constant.StatusConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.exception.AccountLockException;
import com.skytakeaway.common.exception.AccountNotFoundException;
import com.skytakeaway.common.exception.PasswordErrorException;
import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.pojo.dto.EmployeeDTO;
import com.skytakeaway.pojo.dto.EmployeeLoginDTO;
import com.skytakeaway.pojo.dto.EmployeePageQueryDTO;
import com.skytakeaway.pojo.entity.Employee;
import com.skytakeaway.server.mapper.EmployeeMapper;
import com.skytakeaway.server.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO){
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        Employee employee = employeeMapper.getByUsername(username);

        //if employee is not found in the employee_table
        if(employee == null){
            throw new AccountNotFoundException(MessageConstant.ACOCUNT_NOT_FOUND);
        }

        //compare password
        //convert password with md5
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if(!password.equals(employee.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //check the status of the account
        if(employee.getStatus() == StatusConstant.DISABLE){
            throw new AccountLockException(MessageConstant.ACCOUNT_LOCK);
        }

        return employee;
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //copy data in employeeDTO to employee
        BeanUtils.copyProperties(employeeDTO, employee);

        //set initial status for account
        employee.setStatus(StatusConstant.ENABLE);

        //set encrypted password for account
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employeeMapper.addEmployee(employee);
    }

    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //page helper can help to join limit {page number},{page size} to sql
        PageHelper.startPage(employeePageQueryDTO.getPageNumber(),employeePageQueryDTO.getPageSize());
        Page<Employee> result = employeeMapper.pageQuery(employeePageQueryDTO.getName());

        return new PageResult<>(result.getTotal(), result.getResult());
    }

    @Override
    public void modifyEmployeeStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.updateEmployee(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeMapper.deleteEmployeeById(id);
    }

}
