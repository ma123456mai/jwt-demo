package com.jwt.demo.search;

import com.jwt.demo.modle.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author   Mr丶s
 * @ClassName
 * @Version   V1.0
 * @Date   2018/8/29 16:10
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearch{

    /**
     * 用户
     */
    User user;

    /**
     * 用户主键
     */
    Long id;

    /**
     * 用户名
     */
    String username;

    /**
     * 密码(加密后)
     */
    String password;

    /**
     * 验证码
     */
    String captcha;

    /**
     * 是否需要验证码验证
     */
    boolean captchaValidate = true;

    /**
     * 服务端验证码
     */
    Object serviceVCode;

    /**
     * 创建时间
     */
    Date gmtCreated;

    /**
     * 修改时间
     */
    Date gmtUpdated;
}