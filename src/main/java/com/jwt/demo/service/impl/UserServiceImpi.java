package com.jwt.demo.service.impl;

import com.jwt.demo.dao.UserDao;
import com.jwt.demo.modle.User;
import com.jwt.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mr丶s
 * @ClassName UserServiceImpi
 * @Version V1.0
 * @Date 2018/10/11 10:25
 * @Description
 */
@Slf4j
@Service
public class UserServiceImpi implements UserService {

    @Autowired
    UserDao userDao;

    /**
     * 根据用户名查询
     * @param user
     * @return
     */
    @Override
    public User findByUsername(User user) {
        return userDao.findByUsername(user.getUsername());
    }

    /**
     * 根据用户id查查询
     * @param userId
     * @return
     */
    @Override
    public User findUserById(String userId) {
        return userDao.findUserById(userId);
    }
}
