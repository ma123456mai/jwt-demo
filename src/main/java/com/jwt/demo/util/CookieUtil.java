package com.jwt.demo.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  CookieUtil
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/10/10 9:23
 * @Description
 */
public class CookieUtil {

    /**
     * 查找特定cookie值
     *
     * @param cookieName 名称
     * @param request    请求
     * @return 值
     */
    public static String getCookie(String cookieName, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * @param cookieName 名称
     * @param response   返回reponse
     * @param path       路径
     */
    public static void deleteCookie(String cookieName, HttpServletResponse response, String path) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        if (path != null) {
            cookie.setPath("/");
        }
        response.addCookie(cookie);
    }
}
