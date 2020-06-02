package org.ning.cache.controller;

import org.ning.cache.entity.Employee;
import org.ning.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author WangNing
 * @version 1.0
 * @date 2020/6/1 13:25
 */



@RestController
@RequestMapping("/api/v3")
public class CacheController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/get_employee/{id}")
    public @ResponseBody Employee getEmployee(@PathVariable("id") long id){
        return employeeService.getEmp(id);
    }

    @GetMapping("/update_employee/{id}")
    public @ResponseBody Employee updateEmployee(@PathVariable("id") long id){
        Employee employee = new Employee();
        employee.setId(id);
        employee.setLastName("-update-");
        return employeeService.updateEmp(employee);
    }

    @GetMapping("/delete_employee/{id}")
    public @ResponseBody Boolean deleteEmployee(@PathVariable("id") long id){
        return employeeService.deleteEmp(id);
    }

    @GetMapping("/query_lastName/{lastName}")
    public @ResponseBody Employee deleteEmployee(@PathVariable("lastName") String lastName){
        return employeeService.getEmployeeByLastName(lastName);
    }


}
