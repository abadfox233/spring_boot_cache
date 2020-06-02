package org.ning.cache.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ning.cache.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeDao {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    @Select("select * from `employee` where last_name = #{lastName}")
    Employee selectByLastName(@Param("lastName") String lastName);
}