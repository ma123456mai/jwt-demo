package com.jwt.demo.service;

import com.jwt.demo.modle.User;
import com.jwt.demo.result.Result;
import com.jwt.demo.search.UserSearch;

/**
 * @author   Mr丶s
 * @ClassName
 * @Version   V1.0
 * @Date   2018/10/9 21:54
 * @Description
 */

public interface LoginService {

    /**
     * 登录
     *
     * @param search 参数
     * @return 用户
     */
    Result<User> login(UserSearch search);
}
