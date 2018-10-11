package com.jwt.demo.util;

import com.alibaba.fastjson.JSON;
import com.jwt.demo.filter.WrappedHttpServletRequest;
import com.jwt.demo.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author  SignUtil
 * @ClassName Mr丶s
 * @Version   V1.0
 * @Date   2018/10/10 15:05
 * @Description
 */
@Slf4j
public class SignUtil {
    private static DecimalFormat dfm = new DecimalFormat("##0.00");
    /**
     * 签名字段
     */
    private static String SIGN = "sign";
    /**
     * 时间戳字段
     */
    private static String TIME = "time";
    /**
     * api 接口签名key
     */
    private static String API_SIGN_MD5_KEY = "coron_api_2017";
    /**
     * api sign调用过期时间 秒
     */
    private static int SING_EXP_TIME = 5;

    /**
     * 获取签名
     *
     * @param str     待签名的字符串
     * @param mad5Key key值
     * @return 签名结果
     */
    private static String getSign(String str, String mad5Key) {
        String waitSignStr = str + "&key=" + mad5Key;
        return Md5Util.md5_32(waitSignStr);
    }

    /**
     * 获取签名(最终mad5后)
     *
     * @param target  参数对象
     * @param mad5Key 签名mad5值
     * @return sign后的值
     */
    public static String getSign(Object target, String mad5Key) {
        try {
            String signResult = getSign(getSignStr(target), mad5Key);
            if (StringUtil.isNullStr(signResult)) {
                return null;
            }
            return signResult.toUpperCase();
        } catch (Exception e) {
            log.error("getSign 异常", e);
        }
        return null;
    }

    /**
     * 从请求中获取参数，并且按照字段顺序排序后进行签名
     *
     * @param requestWrapper 请求
     * @param mad5Key        md5 key
     * @return 签名后的字符串
     */
    public static String getSign(WrappedHttpServletRequest requestWrapper, String mad5Key) throws IOException {
        Map<String, Object> param = getParamMap(requestWrapper);
        SignContext signC = getSign(param, mad5Key);
        return signC.getSignStr();
    }

    /**
     * @param param   参数
     * @param mad5Key 加密key
     * @return
     * @throws IOException 流异常
     */
    public static SignContext getSign(Map<String, Object> param, String mad5Key) throws IOException {
        deleteMapByKey(param, SIGN);
        String paramsStr = map2Str(sortMap(param, true));
        String signStr = getSign(paramsStr, mad5Key);
        return new SignContext(paramsStr, signStr);
    }

    /**
     * 验证签名
     *
     * @param requestWrapper 请求
     * @return 验证结果
     */
    public static Result verifyApiSign(WrappedHttpServletRequest requestWrapper) {

        try {
            Map<String, Object> param = getParamMap(requestWrapper);
            if (null == param || param.isEmpty()) {
                return Result.fail("没有传递任何参数");
            }

            String paramSign = (String) param.get(SIGN);
            if (StringUtil.isNullStr(paramSign)) {
                return Result.fail("未传递签名参数");
            }

            String timeStr = String.valueOf(param.get(TIME));
            if (StringUtil.isNullStr(timeStr) || "null".equals(timeStr)) {
                return Result.fail("未传递时间字段或者值为空");
            }

            Long time = Long.parseLong(timeStr);
            Long serTime = System.currentTimeMillis() / 1000;
            if (serTime - time > SING_EXP_TIME) {
                return Result.fail("请求过期");
            }

            SignContext signC = getSign(param, API_SIGN_MD5_KEY);
            String serviceSign = signC.getSignStr();
            if (paramSign.toUpperCase().equals(serviceSign.toUpperCase())) {
                return Result.success("验证签名通过");
            } else {
                return Result.fail("验证签名失败,待签名参数列表【" + signC.getOriginalStr() + "】");
            }
        } catch (Throwable e) {
            return Result.fail(e.getMessage());
        }

    }

    /**
     * 根据key删除map项
     *
     * @param map      待删除map
     * @param keyValue key值
     */
    @SuppressWarnings("unchecked")
    private static void deleteMapByKey(Map map, String keyValue) {
        if (null == map || StringUtil.isNullStr(keyValue)) {
            return;
        }
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (keyValue.equals(key)) {
                iter.remove();
            }
        }
    }

    /**
     * 从请求中获取参数map
     *
     * @param requestWrapper 请求
     * @return 参数map
     */
    private static Map<String, Object> getParamMap(WrappedHttpServletRequest requestWrapper) {
        if ("POST".equals(requestWrapper.getMethod().toUpperCase())) {
            return getPostParamMap(requestWrapper);
        } else {
            return getGetParamMap(requestWrapper);
        }
    }

    /**
     * 获取post请求参数
     *
     * @param requestWrapper 请求
     * @return 参数map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getPostParamMap(WrappedHttpServletRequest requestWrapper) {
        try {
            String params = requestWrapper.getRequestParams();
            if (StringUtil.isNotNullStr(params)) {
                return JSON.parseObject(params, HashMap.class);
            }
        } catch (IOException e) {
            log.error("获取参数异常", e);
        }
        return Collections.emptyMap();
    }

    /**
     * 获取get请求参数
     *
     * @param requestWrapper 请求
     * @return 参数map
     */
    private static Map<String, Object> getGetParamMap(WrappedHttpServletRequest requestWrapper) {
        // 参数Map
        Map properties = requestWrapper.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<>();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name;
        String value = null;
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (String s : values) {
                    value = s + ",";
                }
                if (StringUtil.isNotNullStr(value)) {
                    value = value.substring(0, value.length() - 1);
                }
            } else {
                value = valueObj.toString();
            }
            if (StringUtil.isNotNullStr(name) && StringUtil.isNotNullStr(value)) {
                returnMap.put(name, value);
            }
        }
        return returnMap;
    }

    /**
     * 按照字典顺序获取签名字符串
     *
     * @param target 参数对象
     * @return 排序好的签名字符串
     */
    private static String getSignStr(Object target) {
        return getSignStr(target, true);
    }

    /**
     * 将参数对象转换成待签名的key-value字符串
     *
     * @param target 参数对象
     * @param asc    排序
     * @return 待签名字符串
     */
    private static String getSignStr(Object target, final boolean asc) {
        Map<String, Object> map = getSignMap(target, asc);
        return map2Str(map);
    }

    /**
     * 将map转化为待签名的字符串
     *
     * @param map map
     * @return 字符串
     */
    private static String map2Str(Map<String, Object> map) {
        if (null == map || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        String resutl = sb.toString();
        return resutl.substring(0, resutl.length() - 1);
    }

    /**
     * 排序
     *
     * @param target 参数对象
     * @param asc    排序
     * @return 排序后的签名字段map
     */
    private static Map<String, Object> getSignMap(Object target, final boolean asc) {
        Map<String, Object> map = getSignMap(target);
        return sortMap(map, asc);
    }

    /**
     * map 排序
     *
     * @param map 待排序map
     * @param asc true-升序，false-降序
     * @return 排序后的map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> sortMap(Map<String, Object> map, final boolean asc) {
        if (null == map || map.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> sortMap = new TreeMap((o1, o2) -> {
            String str1 = (String) o1;
            String str2 = (String) o2;
            if (asc) {
                return str1.compareTo(str2);
            } else {
                return str2.compareTo(str1);

            }
        });
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 获取签名字段以及值
     *
     * @param target 参数对象
     * @return 签名字段map
     */
    private static Map<String, Object> getSignMap(Object target) {
        if (target == null) {
            return Collections.emptyMap();
        }
        String methodName;
        Map<String, Object> map = new HashMap<>();
        try {
            Method[] methods = target.getClass().getMethods();
            for (Method method : methods) {
                methodName = method.getName();
                if (!methodName.equals("getClass") && methodName.startsWith("get")
                        && method.isAnnotationPresent(SignField.class)) {
                    String field = methodName.substring(3, 4).toLowerCase()
                            + methodName.substring(4);
                    Object value = method.invoke(target);
                    boolean isSignField = false;
                    if (method.isAnnotationPresent(SignField.class)) {//判断指定类型的注释是否存在于此元素上
                        isSignField = true;
                        SignField signField = method.getAnnotation(SignField.class);
                        if (!"".equals(signField.name())) {
                            field = signField.name();
                        }
                        if (!"".equals(signField.value())) {
                            value = signField.value();
                        }
                    }
                    //不是签名字段，结束当前循环
                    if (!isSignField) {
                        continue;
                    }
                    //值为空，结束当前循环
                    if (null == value) {
                        continue;
                    }
                    if (value instanceof Number) {
                        if (value instanceof Double) {
                            map.put(field, dfm.format(value));
                        } else {
                            map.put(field, value);
                        }
                    } else if (value instanceof Date) {
                        String strDate = Date2.format((Date) value);
                        map.put(field, strDate);
                    } else {
                        map.put(field, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("序列化获取签名字段异常", e);
        }
        return map;
    }
}
