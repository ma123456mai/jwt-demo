package com.jwt.demo.util;

import com.jwt.demo.manager.LoginManager;
import com.jwt.demo.manager.TokenManager;
import com.jwt.demo.modle.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author UserUtil
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/10 21:16
 * @Description
 */
@Slf4j
@Data
@Component
public class UserUtil {

    @Autowired
    private LoginManager manager;

    //配置注入
    private static LoginManager loginManager;

    @PostConstruct
    public void init(){
        loginManager = manager;
    }

    /**
     * 获取用户
     *
     * @param request 请求
     * @return 登录用户
     */
    public static User getUser(HttpServletRequest request) {
        User user = (User) request.getAttribute(TokenManager.USER);
        if (null == user) {
            String vt = getVt(request);
            user = loginManager.getUserFromRedis(vt);
        }
        return user;
    }

    /**
     * 获取用户 id
     *
     * @param request 请求
     * @return 登录用户 id
     */
    public static Long getUserId(HttpServletRequest request) {
        User user = getUser(request);
        if (null != user) {
            return user.getId();
        }
        return null;
    }

    /**
     * 获取vt
     *
     * @param request 请求
     * @return vt
     */
    public static String getVt(HttpServletRequest request) {
        String vt = (String) request.getAttribute(TokenManager.VT);
        if (StringUtil.isNullStr(vt)) {
            vt = CookieUtil.getCookie(TokenManager.VT, request);
        }
        return vt;
    }

}

