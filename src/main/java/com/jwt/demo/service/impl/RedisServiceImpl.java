package com.jwt.demo.service.impl;

import com.jwt.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author  redis操作
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/11 10:54
 * @Description
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Integer> integerRedisTemplate;

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;


    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void setStringValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setStringValue(String key, String value, long timeout, TimeUnit timeUnit) {
        if (timeout > 0) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } else {
            setStringValue(key, value);
        }
    }

    @Override
    public void setIntegerValue(String key, int value) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    @Override
    public void setIntegerValue(String key, int value, long timeout, TimeUnit timeUnit) {
        if (timeout > 0) {
            stringRedisTemplate.opsForValue().set(key, String.valueOf(value), timeout, timeUnit);
        } else {
            setIntegerValue(key, value);
        }
    }

    @Override
    public void setLongValue(String key, long value) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    @Override
    public void setLongValue(String key, long value, long timeout, TimeUnit timeUnit) {
        if (timeout > 0) {
            stringRedisTemplate.opsForValue().set(key, String.valueOf(value), timeout, timeUnit);
        } else {
            setLongValue(key, value);
        }
    }

    @Override
    public String updateStringValue(String key, String newValue) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        if (oldValue == null) {
            stringRedisTemplate.opsForValue().set(key, newValue);
        } else {
            long expire = stringRedisTemplate.getExpire(key, TimeUnit.MICROSECONDS);
            if (expire > 0) {
                stringRedisTemplate.opsForValue().set(key, newValue, expire, TimeUnit.MICROSECONDS);
            } else {
                stringRedisTemplate.opsForValue().set(key, newValue);
            }
        }
        return oldValue;
    }

    @Override
    public Integer updateIntegerValue(String key, int newValue) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        if (oldValue == null) {
            stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue));
            return null;
        } else {
            long expire = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
            if (expire > 0) {
                stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue), expire, TimeUnit.MILLISECONDS);
            } else {
                stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue));
            }
            return Integer.valueOf(oldValue);
        }
    }

    @Override
    public Long updateLongValue(String key, long newValue) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        if (oldValue == null) {
            stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue));
            return null;
        } else {
            long expire = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
            if (expire > 0) {
                stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue), expire, TimeUnit.MILLISECONDS);
            } else {
                stringRedisTemplate.opsForValue().set(key, String.valueOf(newValue));
            }
            return Long.valueOf(oldValue);
        }
    }

    @Override
    public Integer increaseIntegerValue(String key, int increment) {
        Long newValue = integerRedisTemplate.opsForValue().increment(key, increment);
        if (newValue == null) {
            return null;
        } else {
            return newValue.intValue();
        }
    }

    @Override
    public Long increaseLongValue(String key, long increment) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        longRedisTemplate.opsForValue().increment(key, increment);
        if (oldValue == null) {
            return null;
        } else {
            return Long.valueOf(oldValue);
        }
    }

    @Override
    public Integer decreaseIntegerValue(String key, int decrement) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        integerRedisTemplate.opsForValue().increment(key, -decrement);
        if (oldValue == null) {
            return null;
        } else {
            return Integer.valueOf(oldValue);
        }
    }

    @Override
    public Long decreaseLongValue(String key, long decrement) {
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        longRedisTemplate.opsForValue().increment(key, -decrement);
        if (oldValue == null) {
            return null;
        } else {
            return Long.valueOf(oldValue);
        }
    }

    @Override
    public String getStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Integer getIntegerValue(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        } else {
            return Integer.valueOf(value);
        }
    }

    @Override
    public Long getLongValue(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        } else {
            return Long.valueOf(value);
        }
    }


}
