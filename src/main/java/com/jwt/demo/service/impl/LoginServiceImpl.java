package com.jwt.demo.service.impl;

import com.jwt.demo.dao.UserDao;
import com.jwt.demo.enums.ErrorEnum;
import com.jwt.demo.modle.User;
import com.jwt.demo.result.Result;
import com.jwt.demo.search.UserSearch;
import com.jwt.demo.service.LoginService;
import com.jwt.demo.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @ClassName
 * @Version   V1.0
 * @Date   2018/8/28 17:01
 * @Description
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserDao userDao;

    @Override
    public Result<User> login(UserSearch search) {
        //服务端验证码
        if (search.isCaptchaValidate()) {
            String sessionCode = (String) search.getServiceVCode();
            if (!search.getCaptcha().equalsIgnoreCase(sessionCode)) {
                return Result.fail("验证码不正确", ErrorEnum.LOGIN_VALIDATE_CODE_ERROR.getValue());
            }

        }
        User user = userDao.findByUsername(search.getUsername());

        if (null == user) {
            return Result.fail("用户不存在", ErrorEnum.USER_NOT_EXIT.getValue());
        }
        String dbPassword = user.getPassword();
        dbPassword = Md5Util.md5_32(dbPassword + search.getServiceVCode());

        if (!dbPassword.equals(search.getPassword())) {
            return Result.fail("密码错误", ErrorEnum.USER_PASSWORD_ERROR.getValue());
        }
        search.setUser(user);
        return Result.success(user);
    }

}
