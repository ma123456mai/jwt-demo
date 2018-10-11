package com.jwt.demo.dao;

import com.jwt.demo.modle.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author Mr丶s
 * @ClassName UserDao
 * @Version V1.0
 * @Date 2018/10/11 10:19
 * @Description
 */
@Mapper
@Component(value = "User")
public interface UserDao {
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    User findByUsername(@Param("username") String username);

    /**
     * 根据用户id查查询
     * @param Id
     * @return
     */
    User findUserById(@Param("Id") String Id);
}
