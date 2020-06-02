package org.ning.cache.service;

import org.ning.cache.entity.Employee;

/**
 * @author WangNing
 * @version 1.0
 * @date 2020/6/1 13:37
 */
public interface EmployeeService {

    Employee getEmp(Long id);

    Employee updateEmp(Employee employee);

    boolean deleteEmp(Long id);

    Employee getEmployeeByLastName(String lastName);

}
