package org.ning.cache.keyGenerator;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author WangNing
 * @version 1.0
 * @date 2020/6/1 14:20
 */
@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        if(objects.length ==0){
            return new CustomKey();
        }else {
            for(Object object:objects){
                if(null != object){
                    return object;
                }
            }

            return method.getName();

        }

    }

    public static class CustomKey{

    }

}
