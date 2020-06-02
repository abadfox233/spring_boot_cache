package org.ning.cache.dao;

import org.ning.cache.entity.Department;
import org.springframework.stereotype.Component;

@Component
public interface DepartmentDao {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

}