package com.jwt.demo.manager;



import com.jwt.demo.modle.User;
import com.jwt.demo.result.Result;
import com.jwt.demo.search.UserSearch;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author   Mr丶s
 * @ClassName
 * @Version   V1.0
 * @Date   2018/8/29 16:16
 * @Description
 */
public interface LoginManager {

    /**
     * 将用户保存到redis
     *
     * @param vt      唯一key
     * @param user    用户
     * @param timeout 过期时间,分钟
     */
    void saveUser2Redis(String vt, User user, Integer timeout);

    /**
     * 从redis获取用户
     *
     * @param vt 存储key
     * @return 用户
     */
    User getUserFromRedis(String vt);

    /**
     * 更新时间
     *
     * @param vt   唯一key
     * @param user 用户
     */
    void updateUser2Redis(String vt, User user);

    /**
     * 注销用户
     *
     * @param vt 唯一key
     */
    void logoutFromRedis(String vt);

    /**
     * 验证有效性
     *
     * @param vt 唯一key
     * @return 用户
     */
    User validate(String vt);

    /**
     * 将验证码保存到redis
     *
     * @param key  唯一key
     * @param code 验证码
     */
    void saveValidateCode(String key, String code);

    /**
     * 刷新JWT Token
     *
     * @param user 用户
     * @return Token结果
     */
    Result getJWTToken(User user);

    /**
     * 获取验证码
     *
     * @param key 唯一值
     * @return 验证码
     */
    String getValidateCode(String key);

    /**
     * 构建JWT 过期时间
     *
     * @param minute 分钟
     * @return 日期
     */
    Date createJWTExpTime(int minute);

    /**
     * JWT 登录
     *
     * @param userSearch 请求参数
     * @return 登录结果
     */
    Result loginOfJTW(UserSearch userSearch);


    /**
     * 创建一个验证码，保存到redis，然后返回
     *
     * @return 验证码
     */
    String createValidateCode();

    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return 登录用户
     */
    Result getLoginUser(HttpServletRequest request);

    /**
     * 获取用户绑定到redis的JWT Token
     *
     * @param userId 用户id
     * @return JWT token
     */
    String getUserToken(String userId);

    /**
     * 失效Token
     *
     * @param userId 用户id
     */
    void invalidToken(Long userId);

}
