package com.jwt.demo.service;


import com.jwt.demo.modle.User;

/**
 * @author Mr丶s
 * @ClassName UserService
 * @Version V1.0
 * @Date 2018/10/11 10:22
 * @Description
 */
public interface UserService {

    /**
     * 根据用户名查询
     * @param user
     * @return
     */
    User findByUsername(User user);

    /**
     * 根据用户id查查询
     * @param userId
     * @return
     */
    User findUserById(String userId);

}
