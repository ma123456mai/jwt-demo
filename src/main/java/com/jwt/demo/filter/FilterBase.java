package com.jwt.demo.filter;

import com.jwt.demo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  FilterBase
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/10/9 14:59
 * @Description   
 */
@Slf4j
public abstract class FilterBase {

    /**
     * 不需要拦截的URI模式，以正则表达式表示
     */
    @Autowired
    protected String excludes;
    /**
     * 连接中的白名单
     */
    @Autowired
    protected List<String> whiteList = new ArrayList<>();
    /**
     * 签名列表的请求
     */
    @Autowired
    protected List<String> signList = new ArrayList<>();

    /**
     * 初始化白名单信息
     *
     * @param whiteList
     */
    @Autowired
    abstract void initWhiteList(List<String> whiteList);

    /**
     * 签名列表
     *
     * @param signList
     */
    @Autowired
    abstract void initSignList(List<String> signList);

    /**
     * 初始化
     *
     * @param config 配置
     */
    @Autowired
    protected void baseInit(FilterConfig config) {
        excludes = config.getInitParameter("excludes");
        initWhiteList(whiteList);
        initSignList(signList);
    }

    /**
     * 判断请求是否不需要拦截
     *
     * @param request 请求
     * @return 判断结果
     */
    protected boolean requestIsExclude(ServletRequest request) {
        if (hasWhiteList(request)) {
            return true;
        }
        // 没有设定excludes时，所以经过filter的请求都需要被处理
        if (StringUtil.isNullStr(excludes)) {
            return false;
        }

        // 获取去除context path后的请求路径
        String contextPath = request.getServletContext().getContextPath();
        String uri = ((HttpServletRequest) request).getRequestURI();
        uri = uri.substring(contextPath.length());

        // 正则模式匹配的uri被排除，不需要拦截
        boolean isExcluded = uri.matches(excludes);

        if (isExcluded) {
            log.debug("request path: {} is excluded!", uri);
        }

        return isExcluded;
    }

    /**
     * 检测是否含有白名单数据
     *
     * @return 是否在白名单中
     */
    private boolean hasWhiteList(ServletRequest request) {
        String contextPath = getContextPath(request);
        for (String wl : whiteList) {
            if (contextPath.endsWith(wl)) {
                return true;
            }
            if (contextPath.startsWith(wl)) {
                return true;
            }
        }
        return false;
    }

    protected String getContextPath(ServletRequest request) {
        String contextPath = ((HttpServletRequest) request).getRequestURI();
        return contextPath.trim();
    }

    /**
     * 检测是否含有签名数据
     *
     * @return 是否在签名单中
     */
    protected boolean hasSignList(ServletRequest request) {
        String contextPath = ((HttpServletRequest) request).getRequestURI();
        contextPath = contextPath.trim();
        for (String wl : signList) {
            if (contextPath.endsWith(wl)) {
                return true;
            }
        }
        return false;
    }
}
