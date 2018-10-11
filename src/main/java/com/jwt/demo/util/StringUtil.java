package com.jwt.demo.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author  Mr丶s
 * @ClassName   
 * @Version   V1.0
 * @Date   2018/10/9 17:44
 * @Description   
 */
public class StringUtil {
    /**
     * 判断字符是否为空
     *
     * @param str 需要判断的字符串
     * @return boolean true:空;false:不为空
     * @add by Nick Yau
     */
    public static boolean isNullStr(String str) {
        return (null == str || str.length() < 1);
    }

    /**
     * 是否非空
     *
     * @param str 待判断字符串
     * @return 字符串
     */
    public static boolean isNotNullStr(String str) {
        return !isNullStr(str);
    }

    /**
     * 编码
     *
     * @param str 待编码字符串
     * @param enc 编码格式
     * @return 字符串
     */
    public static String encode(String str, String enc) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 解码
     *
     * @param str 待解码字符串
     * @param enc 解码格式
     * @return 字符串
     */
    public static String decode(String str, String enc) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 去掉字符串前置空格
     *
     * @param in 字符串
     * @return String 字符串
     */
    public static String ltrim(String in) {
        if (in == null) {
            return null;
        }
        while (in.substring(0, 1).equals(" ")) {
            in = in.substring(1, in.length());
        }
        return in;
    }

    /**
     * 去掉字符串后置空格
     *
     * @param in 字符串
     * @return String 字符串
     */
    public static String rtrim(String in) {
        if (in == null) {
            return null;
        }
        while (in.substring(in.length() - 1, in.length()).equals(" ")) {
            in = in.substring(0, in.length() - 1);
        }
        return in;
    }

    /**
     * 去掉字符串前后置空格
     *
     * @param in 字符串
     * @return String 字符串
     */
    public static String trim(String in) {
        if (in == null) {
            return null;
        }
        while (in.substring(in.length() - 1, in.length()).equals(" ")) {
            in = in.substring(0, in.length() - 1);
        }
        while (in.substring(0, 1).equals(" ")) {
            in = in.substring(1, in.length());
        }

        return in;
    }

    /**
     * 去掉字符串数组前后置空格
     *
     * @return String 字符串数组
     */
    public static String[] trim(String[] arr) {
        if (arr == null || arr.length < 1) {
            return null;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = trim(arr[i]);
        }
        return arr;
    }

    /**
     * 一维数组组装成带分隔符的字符串
     *
     * @param array 一维数组
     * @return String 字符串
     * @add by Nick Yau
     */
    public static String array2String(String[] array, String prefix) {

        StringBuilder builder = new StringBuilder();

        for (Object str : array) {
            builder.append(str).append(prefix);
        }

        return builder.toString().substring(0, builder.toString().length() - 1);
    }

    /**
     * 将字符串转换成大写
     *
     * @param str 待转字符串
     * @return 字符串
     */
    public static String greatString(String str) {
        String upStr = str.toUpperCase();
        StringBuffer buf = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.append(upStr.charAt(i));
        }
        return buf.toString();
    }

    /**
     * 将字符串转换成小写 @param str 字符串
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String smallString(String str) {
        String upLower = str.toLowerCase();
        StringBuffer buf = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.append(upLower.charAt(i));
        }
        return buf.toString();
    }

    /**
     * 字符串大小写互转
     *
     * @param str
     * @return 字符串
     */
    public static String convertString(String str) {
        String upStr = str.toUpperCase();
        String lowStr = str.toLowerCase();
        StringBuffer buf = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == upStr.charAt(i)) {
                buf.append(lowStr.charAt(i));
            } else {
                buf.append(upStr.charAt(i));
            }
        }
        return buf.toString();
    }

    /**
     * 判断字符串是否全为数字
     *
     * @param str 需要比较的字符串
     * @return boolean true:是数字;false:不是数字
     * @add by Nick Yau
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = compile("[-]?\\d+[.]?\\d*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 防止特殊字符(‘)sql注入
     *
     * @param sqlParam
     * @return 字符串
     */
    public static String formatBaseQueryParam(String sqlParam) {
        return sqlParam.replace("'", "''");
    }

    /**
     * Like 查询条件sql参数特殊字符(',[,%,_,^)转译
     *
     * @param sqlParam
     * @return 字符串
     */
    public static String formatLikeQueryParam(String sqlParam) {
        return sqlParam.replace("'", "''").replace("[", "[[]")
                .replace("%", "[%]").replace("_", "[_]").replace("^", "[^]");
    }

    /**
     * 向url后追加参数，拼接时需要判断连接符是? or &，同时需要对参数值进行编码
     *
     * @param origUrl       原始url
     * @param parameterName 参数名
     * @param parameterVal  参数值
     * @return 新字符串
     */
    public static String appendUrlParameter(String origUrl, String parameterName, String parameterVal) {
        if (origUrl == null) {
            return null;
        }

        String bound = origUrl.contains("?") ? "&" : "?";
        StringBuilder sb = new StringBuilder(origUrl);
        sb.append(bound);
        sb.append(parameterName).append("=");
        try {
            sb.append(URLEncoder.encode(parameterVal, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return sb.toString();
    }

    /**
     * 利用UUID生成全局唯一32位长的hex字符串
     *
     * @return 字符串
     */
    public static String uniqueKey() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    // 将所有请求参数重新拼接成queryString
    public static String makeQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        Enumeration<String> paraNames = request.getParameterNames();
        while (paraNames.hasMoreElements()) {
            String paraName = paraNames.nextElement();
            String[] paraVals = request.getParameterValues(paraName);
            for (String paraVal : paraVals) {
                builder.append("&").append(paraName).append("=").append(URLEncoder.encode(paraVal, "utf-8"));
            }
        }

        if (builder.length() > 0) {
            builder.replace(0, 1, "?");
        }

        return builder.toString();
    }

    /**
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
