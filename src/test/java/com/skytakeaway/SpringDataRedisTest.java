package com.skytakeaway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);

        //String operation
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //SET KEY VALUE EXPIRE
        //valueOperations.set("code","123", 1, TimeUnit.MINUTES);
        //GET KEY
        //String code = (String) valueOperations.get("code");
        //System.out.println(code);

        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        if(keys != null){
            keys.forEach(key -> redisTemplate.delete(key));
        }
    }
}
