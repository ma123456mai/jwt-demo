package com.jwt.demo.config;

import com.jwt.demo.handle.Impl.CaptchaPreLoginHandler;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author   Mr丶s
 * @ClassName   
 * @Version   V1.0
 * @Date   2018/8/29 21:54
 * @Description   
 */
@Data
@Component
public class LoginConfig {
    /**
     * 验证码过期时间
     */
    private Integer validateCodeTimeOut = 5;

    /**
     * 登录页面视图名称
     */
    private String loginViewName = "/login";

    /**
     * api JWT 过期时间 分钟
     */
    private int jwtTimeout = 1 * 24 * 60;

    /**
     * 验证码处理
     */
    private CaptchaPreLoginHandler captchaPreLoginHandler = new CaptchaPreLoginHandler();


}
