package org.ning.cache;

import org.junit.jupiter.api.Test;
import org.ning.cache.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class CacheApplicationTests {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
//        stringRedisTemplate.opsForValue().append("kkk", "Hello");
//        System.out.println(stringRedisTemplate.opsForValue().get("kkk"));

//        System.out.println(redisTemplate.opsForValue().get("kkk"));
        redisTemplate.opsForValue().set("key", "Hello World", 10, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("key"));
        Employee employee = new Employee();
        employee.setLastName("ningwang");
        employee.setId(666L);
        redisTemplate.opsForValue().set("employee-666", employee);
        Employee employee1 = (Employee) redisTemplate.opsForValue().get("employee-666");
        System.out.println(employee1);

    }

}
