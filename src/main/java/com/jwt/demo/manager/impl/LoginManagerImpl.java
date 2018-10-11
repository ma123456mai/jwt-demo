package com.jwt.demo.manager.impl;
import com.alibaba.fastjson.JSON;
import com.jwt.demo.config.LoginConfig;
import com.jwt.demo.dto.JWTContext;
import com.jwt.demo.enums.ErrorEnum;
import com.jwt.demo.handle.Impl.CaptchaPreLoginHandler;
import com.jwt.demo.manager.LoginManager;
import com.jwt.demo.manager.TokenManager;
import com.jwt.demo.modle.User;
import com.jwt.demo.pojo.CoronPayload;
import com.jwt.demo.result.Result;
import com.jwt.demo.search.UserSearch;
import com.jwt.demo.service.LoginService;
import com.jwt.demo.service.RedisService;
import com.jwt.demo.util.JWTUtil;
import com.jwt.demo.util.StringUtil;
import com.jwt.demo.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author
 * @ClassName   
 * @Version   V1.0
 * @Date   2018/8/30 9:23
 * @Description   
 */
@Slf4j
@Component("loginManager")
public class LoginManagerImpl implements LoginManager {

    @Autowired
    RedisService redisService;
    @Autowired
    LoginConfig loginConfig;
    @Autowired
    LoginService loginService;


    /**
     * Token保存在redis时候的前缀
     */
    private static final String REDIS_TOKEN_PREFIX = "redis_token_prefix";


    @Override
    public void saveUser2Redis(String vt, User user, Integer timeout) {
        try {
            if (null != timeout) {
                redisService.setStringValue(vt, JSON.toJSONString(user), timeout, TimeUnit.MINUTES);
            } else {
                redisService.setStringValue(vt, JSON.toJSONString(user));
            }
        } catch (Throwable e) {
            log.error("保存用户到redis失败,vt = " + vt + ";user【" + JSON.toJSONString(user) + "】", e);
        }
    }

    @Override
    public User getUserFromRedis(String vt) {
        String userStr = null;
        try {
            userStr = redisService.getStringValue(vt);
            if (StringUtil.isNotNullStr(userStr)) {
                return JSON.parseObject(userStr, User.class);
            }
            return null;
        } catch (Throwable e) {
            log.error("从redis获取用户失败,vt = " + vt + ";user【" + userStr + "】", e);
            return null;
        }

    }

    @Override
    public void updateUser2Redis(String vt, User user) {
        try {
            redisService.updateStringValue(vt, JSON.toJSONString(user));
        } catch (Throwable e) {
            log.error("从redis获取用户失败,vt = " + vt, e);
        }
    }

    @Override
    public void logoutFromRedis(String vt) {
        String value = redisService.getStringValue(vt);
        if (StringUtil.isNotNullStr(value)) {
            redisService.delete(vt);
        }
    }

    /**
     * 验证令牌有效性
     *
     * @param vt 令牌
     * @return 用户
     */
    @Override
    public User validate(String vt) {
        if (vt == null) {
            return null;
        }
        return getUserFromRedis(vt);
    }

    @Override
    public Result getJWTToken(User user) {
        JWTContext context = new JWTContext(user);
        context.setExpiresAt(createJWTExpTime(loginConfig.getJwtTimeout()));
        String token = JWTUtil.createToken(context);
        //保存用户到redis
        saveUser2Redis(context.getJti(), user, loginConfig.getJwtTimeout());
        Long u = user.getId();
        //保存Token到redis
        saveToken2Redis(token, user);
        log.info("token------:"+token);

        return Result.success().setEntry(token);
    }

    /**
     * 保存Token到redis
     *
     * @param token 令牌
     * @param user  登录用户
     */
    private void saveToken2Redis(String token, User user) {
        redisService.setStringValue(REDIS_TOKEN_PREFIX + user.getId(), token);
    }

    /**
     * 获取用户绑定到redis的Token（JWT Token）
     *
     * @param userId 用户id
     * @return JWT令牌
     */
    @Override
    public String getUserToken(String userId) {
        return redisService.getStringValue(REDIS_TOKEN_PREFIX + userId);
    }

    @Override
    public void invalidToken(Long userId) {
        //redis保存的用户失效
        String token = getUserToken(userId + "");
        if (token == null) {
            log.info("让用户【userId=" + userId + "】Token失效的时候未找到对应的Token!");
            return;
        }
        Result<CoronPayload> result = JWTUtil.verify(token);
        if (result.getStatus()) {
            CoronPayload coronPayload = result.getEntry();
            redisService.delete(coronPayload.getJti());

        }

        //用户保存的token失效
        redisService.delete(REDIS_TOKEN_PREFIX + userId);
        log.info("让用户【userId=" + userId + "】Token失效成功");
    }

    /**
     * 创建过期时间
     *
     * @param minute 分钟
     * @return date 日期
     */
    @Override
    public Date createJWTExpTime(int minute) {
        long curren = System.currentTimeMillis();
        curren += minute * 60 * 1000;
        return new Date(curren);
    }

    /**
     * 创建一个验证码，保存到redis，然后返回
     *
     * @return 验证码
     */
    @Override
    public String createValidateCode() {
        CaptchaPreLoginHandler preLoginHandler = loginConfig.getCaptchaPreLoginHandler();

        String code = preLoginHandler.randomCode();
        saveValidateCode(code, code);
        return code;
    }


    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return 登录用户
     */
    @Override
    public Result getLoginUser(HttpServletRequest request) {
        User user =  UserUtil.getUser(request);
        if (null == user) {
            return Result.fail("未获取到登录用户", ErrorEnum.USER_NOT_EXIT.getValue());
        }
        if (user.getPassword() != null) {
            user.setPassword("");
        }
        return Result.success(user);
    }

    /**
     * JWT 登录
     *
     * @param search 请求参数
     * @return 登录结果
     */
    @Override
    public Result loginOfJTW(UserSearch search) {
        search.setCaptchaValidate(true);

        final String serviceVCode = getValidateCode(search.getCaptcha());
        if (StringUtil.isNullStr(serviceVCode)) {
            return Result.fail("验证码过期或者填写错误", ErrorEnum.LOGIN_VALIDATE_CODE_ERROR.getValue());
        }
        search.setServiceVCode(serviceVCode);

        Result<User> result = loginService.login(search);

        //登录成功，生成Token
        if (result.getStatus()) {
            log.info("this log is LoginManagerImpl loginOfJWT login success!");
            User user = result.getEntry();

            log.info("this log is LoginManagerImpl loginOfJWT login user={}", user.toJson());
            return getJWTToken(user);
        }
        return result;
    }


    /**
     * 保存验证码
     *
     * @param key  唯一key
     * @param code 验证码
     */
    @Override
    public void saveValidateCode(String key, String code) {
        redisService.setStringValue(key, code, loginConfig.getValidateCodeTimeOut(), TimeUnit.MINUTES);
    }

    /**
     * 获取redis里面的验证码
     * @param key 唯一值
     * @return
     */
    @Override
    public String getValidateCode(String key) {
        return redisService.getStringValue(key);
    }


}
