package com.jwt.demo.util;

import java.lang.annotation.*;

/**
 * @author   签名字段
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/9 15:21
 * @Description   
 */
 
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SignField {

    //重定义名称
    String name() default "";
    //指定值
    String value() default "";

}
