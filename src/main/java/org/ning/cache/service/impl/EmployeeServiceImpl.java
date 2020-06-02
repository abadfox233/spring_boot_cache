package org.ning.cache.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ning.cache.dao.DepartmentDao;
import org.ning.cache.dao.EmployeeDao;
import org.ning.cache.entity.Employee;
import org.ning.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author WangNing
 * @version 1.0
 * @date 2020/6/1 13:38
 */
/**
 * CacheConfig 抽取缓存公共配置
 */
@CacheConfig(cacheNames = "emp")
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * Cacheable:
     * 1. 根据key规则生成key
     * 2. 查找缓存
     * 3. 如果缓存中存在数据，则返回缓存中的数据。不存在则执行方法，并把方法的返回值缓存起来
     *
     *
     * 参数解释
     * -----------------------------
     * value/cacheNames 缓存名称
     *-------------------------------
     * key: 缓存中的key 支持SpEL表达式
     * keyGenerator: key的生成方式，默认实现 org.springframework.cache.interceptor.SimpleKeyGenerator
     *
     *     可以自定义实现，只需实现接口：org.springframework.cache.interceptor.KeyGenerator
     * 注意:   Key 与 keyGenerator 不能同时定义，否则会无法报错
     *---------------------------------
     * condition 支持SpEL, 当符合条件时才会缓存结果
     * unless 与condition刚好相反，用于否决缓存。符合条件时不会缓存
     * 注意:      只有condition返回true和unless返回false时才会缓存
     *----------------------------------
     * sync: 是否使用异步模式
     *
     *
     * @param id
     * @return
     */
    @Cacheable(value = "emp", /**key = "#root.args[0]" ,**/ keyGenerator = "customKeyGenerator", unless = "#result==null")
    @Override
    public Employee getEmp(Long id) {
        log.info("查询Employee: id {} ", id);
        return employeeDao.selectByPrimaryKey(id);
    }

    /**
     *
     * CachePut:
     * 用于更新数据
     * 先执行方法，然后更新缓存
     *
     *
     * @param employee
     * @return
     */
    @CachePut(value = "emp", key = "#a0.id", cacheManager = "cacheManager")
    @Override
    public Employee updateEmp(Employee employee) {
        log.info("更新Employee: {}", employee);
        int result = employeeDao.updateByPrimaryKeySelective(employee);
        if(result >= 0)
            return employeeDao.selectByPrimaryKey(employee.getId());
        else
            throw new RuntimeException("update error");
    }

    /**
     * CacheEvict 清除缓存
     * @param id
     * @return
     */
    @CacheEvict(value = "emp", key = "#id", allEntries = true, beforeInvocation = true)
    @Override
    public boolean deleteEmp(Long id){
        log.info("删除 employee:{}", id);
        int result = employeeDao.deleteByPrimaryKey(id);
        return result > 0;
    }

    /**
     * 复杂缓存规则
     * @param lastName
     * @return
     */
    @Caching(
        cacheable = {
                @Cacheable(value = "emp", key = "#lastName", unless = "#result==null")
        },
        put = {
                @CachePut(value = "emp", key = "#result.id",condition = "#result!=null")
        }

    )
    @Override
    public Employee getEmployeeByLastName(String lastName){
        log.info("查询LastName:{}",lastName);
        return employeeDao.selectByLastName(lastName);
    }



}
