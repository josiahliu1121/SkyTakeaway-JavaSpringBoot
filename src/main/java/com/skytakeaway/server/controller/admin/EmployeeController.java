package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.constant.JwtClaimsConstant;
import com.skytakeaway.common.properties.JwtProperties;
import com.skytakeaway.common.result.PageResult;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.common.utils.JwtUtils;
import com.skytakeaway.pojo.dto.EmployeeDTO;
import com.skytakeaway.pojo.dto.EmployeeLoginDTO;
import com.skytakeaway.pojo.dto.EmployeePageQueryDTO;
import com.skytakeaway.pojo.entity.Employee;
import com.skytakeaway.pojo.vo.EmployeeLoginVO;
import com.skytakeaway.server.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Employee Interface")
@Slf4j
@RestController()
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    @Operation(summary = "employee login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "Error"),
            @ApiResponse(responseCode = "1", description = "success",
                    headers = @Header(name = "id", description = "1"))
    })
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        Employee employee = employeeService.login(employeeLoginDTO);

        //generate jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtils.generateJwt(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    @PostMapping
    @Operation(summary = "create new employee")
    public Result<Void> addEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "employee page/username query")
    @Parameters({
            @Parameter(name = "name",description = "name",in = ParameterIn.QUERY),
            @Parameter(name = "pageNumber",description = "page number",required = true,in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "page size",required = true,in=ParameterIn.QUERY)
    })
    public Result<PageResult<Employee>> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        PageResult<Employee> pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "activate or block employee account")
    public Result<Void> modifyEmployeeStatus(@PathVariable("status") Integer status,@RequestParam("id") Long id){
        employeeService.modifyEmployeeStatus(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get employee information by id")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }

    @PutMapping
    @Operation(summary = "update employee data")
    public Result<Void> updateEmployee (@RequestBody EmployeeDTO employeeDTO){
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    @DeleteMapping()
    @Operation(summary = "delete employee by id")
    public Result<Void> deleteEmployeeById(@RequestParam("id") Long id){
        employeeService.deleteEmployeeById(id);
        return Result.success();
    }
}
