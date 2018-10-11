package com.jwt.demo.handle;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author  IPreLoginHandler
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/8/30 9:20
 * @Description
 */
public interface IPreLoginHandler {
    String SESSION_ATTR_NAME = "login_session_attr_name";

    /**
     * 前置处理
     */
    Map<?, ?> handle(HttpSession session, String code) throws Exception;
}
