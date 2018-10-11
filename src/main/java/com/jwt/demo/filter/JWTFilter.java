package com.jwt.demo.filter;


import com.jwt.demo.enums.ErrorEnum;
import com.jwt.demo.manager.TokenManager;
import com.jwt.demo.modle.User;
import com.jwt.demo.pojo.CoronPayload;
import com.jwt.demo.result.Result;
import com.jwt.demo.util.JWTUtil;
import com.jwt.demo.util.SignUtil;
import com.jwt.demo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/8/31 15:21
 * @Description   
 */
@Slf4j
public class JWTFilter extends FilterBase implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        baseInit(filterConfig);
    }

    /**
     * 初始化请求白名单(系统)
     */
    @Override
    public void initWhiteList(List<String> whiteList) {
        whiteList.add("apiReqFail");
        whiteList.add("getValidateCode");
        whiteList.add("getPicValidateCode");
        whiteList.add("verifyToken");
        whiteList.add("webStatus");
        whiteList.add("login");
        whiteList.add("register");
        whiteList.add(".jpg");
        whiteList.add(".apk");
        whiteList.add(".zip");
        whiteList.add("/springboor-jwt-demo/swagger-ui.html");
        whiteList.add("/springboor-jwt-demo/v2");
        whiteList.add("/springboor-jwt-demo/api-docs");
        whiteList.add("/springboor-jwt-demo/webjars");
        whiteList.add("/springboor-jwt-demo/swagger-resources");
    }

    @Override
    void initSignList(List<String> signList) {
        signList.add("getPassword");
        signList.add("manageControlPanelByRobot");
        signList.add("checkVersion");
        signList.add("photoMontage");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        WrappedHttpServletRequest requestWrapper = new WrappedHttpServletRequest(request);

        //如果是不需要拦截的请求，直接通过
        if (requestIsExclude(request)) {
            chain.doFilter(requestWrapper, response);
            return;
        }

        //如果是签名接口，则验证签名
        if (hasSignList(request)) {
            Result result = SignUtil.verifyApiSign(requestWrapper);
            if (result.getStatus()) {
                chain.doFilter(requestWrapper, response);
                return;
            } else {
                String location = "/springboor-jwt-demo/apiReqFail?code=" + ErrorEnum.SING_VERIFY_ERROR.getValue() + "&msg=" + URLEncoder.encode(result.getMessage(), "utf-8");
                response.sendRedirect(location);
                return;
            }
        }

        //JWT验证
        String token = request.getHeader(TokenManager.AUTORIZATION);
        log.info("this log is JWTFilter doFilter http header Authorization={}", token);
        if (StringUtil.isNullStr(token)) {
            String location = "/springboor-jwt-demo/apiReqFail?code=" + ErrorEnum.NOT_LOGIN.getValue();
            response.sendRedirect(location);
            return;
        }

        //检测Token本身是否有效
        Result<CoronPayload> result = JWTUtil.verify(token);
        if (!result.getStatus()) {
            String location = "/springboor-jwt-demo/apiReqFail?code=" + getVerifyErrorCode(result) + "&msg=" + URLEncoder.encode(result.getMessage(), "utf-8");
            response.sendRedirect(location);
            return;
        }
        CoronPayload coronPayload = result.getEntry();


        //检测用户存储的Token和当前Token是否一致，如果不一致说明Token已经被更新，挤下线
        boolean tokenUpdated = validateUserTokenAlreadyUpdated(coronPayload, token, "logout", request);
        if (tokenUpdated) {
            String location = "/springboor-jwt-demo/apiReqFail?code=112&msg=" + URLEncoder.encode("账号已经在另一处登录", "utf-8");
            response.sendRedirect(location);
            return;
        }

        //检测用户在redis是否过期
        User user = TokenManager.validateOfJWT(coronPayload);
        if (null == user) {
            String location = "/springboor-jwt-demo/apiReqFail?code=" + ErrorEnum.NOT_LOGIN.getValue();
            response.sendRedirect(location);
            return;
        }

        log.info("JWT请求用户信息: user={}, 用户请求地址: url={}, 用户请求内容: content={}",
                user.getUsername(), request.getRequestURI(), postJsonLog(request));

        writeAttribute(request, user, coronPayload);

        chain.doFilter(requestWrapper, response);
    }


    private String postJsonLog(HttpServletRequest request) {
        String params = null;
        BufferedReader br = null;
        ServletInputStream is = null;
        try {
            is = request.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));

            StringBuilder sb = new StringBuilder("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            params = sb.toString();
            return params;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @param coronPayload JWT主体
     * @param token        令牌
     * @param request      请求
     * @return 结果
     */
    public boolean validateUserTokenAlreadyUpdated(CoronPayload coronPayload, String token, String apiName, ServletRequest request) {
        String contextPath = getContextPath(request);
        if (contextPath.endsWith(apiName)) {
            return false;
        }
        return JWTUtil.validateUserTokenAlreadyUpdated(coronPayload, token);
    }

    /**
     * 获取校验失败code
     *
     * @param result 检测结果
     * @return 对应编码
     */
    private Integer getVerifyErrorCode(Result<CoronPayload> result) {
        String msg = result.getMessage();
        if ("expired".contains(msg)) {
            return ErrorEnum.JWT_HAS_EXPIRED.getValue();
        }
        return ErrorEnum.JWT_VALIDATE_FAIL.getValue();
    }

    /**
     * 将信息写入attribute 供后续业务使用
     *
     * @param request      请求
     * @param user         登录用户
     * @param coronPayload Token 检测结果
     */
    private void writeAttribute(HttpServletRequest request, User user, CoronPayload coronPayload) {
        request.setAttribute(TokenManager.VT, coronPayload.getJti());
        request.setAttribute(TokenManager.USER, user);
        request.setAttribute(TokenManager.UID, coronPayload.getUid());
    }

    @Override
    public void destroy() {

    }


}
