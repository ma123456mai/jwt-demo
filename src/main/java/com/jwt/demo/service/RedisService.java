package com.jwt.demo.service;


import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author   redis操作
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/10/11 10:53
 * @Description
 */
@Component
public interface RedisService {

    /**
     * 删除
     *
     * @param key 唯一key
     */
    void delete(String key);

    /**
     * 设置string类型缓存值
     * 不过期
     *
     * @param key   键
     * @param value 值
     */
    void setStringValue(String key, String value);

    /**
     * 设置string类型缓存值
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    void setStringValue(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 设置int类型缓存值
     * 不过期
     *
     * @param key   键
     * @param value 值
     */
    void setIntegerValue(String key, int value);

    /**
     * 设置int类型缓存值
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    void setIntegerValue(String key, int value, long timeout, TimeUnit timeUnit);

    /**
     * 设置long类型缓存值
     * 不过期
     *
     * @param key   键
     * @param value 值
     */
    void setLongValue(String key, long value);

    /**
     * 设置long类型缓存值
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    void setLongValue(String key, long value, long timeout, TimeUnit timeUnit);

    /**
     * 更新string类型缓存值
     *
     * @param key      键
     * @param newValue 值
     * @return 更新前的值
     */
    String updateStringValue(String key, String newValue);

    /**
     * 更新int类型缓存值
     *
     * @param key      键
     * @param newValue 值
     * @return 更新前的值
     */
    Integer updateIntegerValue(String key, int newValue);

    /**
     * 更新long类型缓存值
     *
     * @param key      键
     * @param newValue 值
     * @return 更新前的值
     */
    Long updateLongValue(String key, long newValue);

    /**
     * 增加int类型缓存值
     *
     * @param key       键
     * @param increment int类型缓存值
     * @return 增加前的值
     */
    Integer increaseIntegerValue(String key, int increment);

    /**
     * 增加long类型缓存值
     *
     * @param key       键
     * @param increment 增加值
     * @return 增加前的值
     */
    Long increaseLongValue(String key, long increment);

    /**
     * 减少int类型缓存值
     *
     * @param key       键
     * @param decrement 减少值
     * @return 减少前的值
     */
    Integer decreaseIntegerValue(String key, int decrement);

    /**
     * 减少long类型缓存值
     *
     * @param key       键
     * @param decrement 减少前的值
     * @return 减少前的值
     */
    Long decreaseLongValue(String key, long decrement);

    /**
     * 获取string类型缓存值
     *
     * @param key 键
     * @return 缓存值
     */
    String getStringValue(String key);

    /**
     * 获取int类型缓存值
     *
     * @param key 键
     * @return int
     */
    Integer getIntegerValue(String key);

    /**
     * 获取long类型缓存值
     *
     * @param key 键
     * @return long类型缓存值
     */
    Long getLongValue(String key);



}
