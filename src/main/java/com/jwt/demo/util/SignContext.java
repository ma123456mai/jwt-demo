package com.jwt.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author  SignContext
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/8 15:06
 * @Description
 */
@Data
@AllArgsConstructor
public class SignContext {
    /**
     * 原始待签名字符串
     */
    private String originalStr;
    /**
     * 签名后的字符串
     */
    private String signStr;
}
