package com.jwt.demo.util;

import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * @author  Date2
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/9 15:18
 * @Description   
 */
public class Date2 {

    /**
     * 默认的pattern
     */
    public static String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期
     *
     * @param date 日期参数
     * @return 返回字符串
     */
    public static String format(Date date) {
        return new LocalDateTime(date).toString(FORMAT_DEFAULT);
    }
}
