package com.jwt.demo.manager;

import com.jwt.demo.modle.User;
import com.jwt.demo.pojo.CoronPayload;
import com.jwt.demo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author  TokenManager
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/10 14:57
 * @Description
 */
@Component
public class TokenManager {

    @Autowired
    private LoginManager manager;

    //配置注入
    private static LoginManager loginManager;

    @PostConstruct
    public void init(){
        loginManager = manager;
    }

    /**
     * token key
     */
    public static String VT = "VT";

    /**
     * 用户id
     */
    public static String UID = "id";
    /**
     * 用户名
     */
    public static String USERNAME = "name";

    /**
     * 储存用户的key
     */
    public static String USER = "curent_user";
    /**
     * 请求头部 JWT Token字段
     */
    public static String AUTORIZATION = "Authorization";

    /**
     * 验证令牌有效性
     *
     * @param coronPayload 主体
     * @return 登录用户
     */
    public static User validateOfJWT(CoronPayload coronPayload) {
        //先判断用户绑定到redis的Token,如果不存在则也认为过期
        String userToken = loginManager.getUserToken(coronPayload.getUid());
        if (StringUtil.isNullStr(userToken)) {
            return null;
        }

        String vt = coronPayload.getJti();
        if (vt == null) {
            return null;
        }
        User user = loginManager.getUserFromRedis(vt);
        if (null != user) {
            loginManager.updateUser2Redis(vt, user);
        }
        return user;
    }
}
