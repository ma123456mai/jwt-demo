package com.jwt.demo.pojo;

import lombok.Data;

/**
 * @author   CoronPayload
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/10/9 21:23
 * @Description   
 */
@Data
public class CoronPayload {
    /**
     * 用户id
     */
    private String uid;

    /**
     * Issuer，发行者
     */
    private String iss;
    /**
     * Subject，主题
     */
    private String sub;
    /**
     * Expiration time，过期时间
     */
    private Long exp;
    /**
     * Not before
     */
    private String nbf;
    /**
     * Issued at，发行时间
     */
    private String iat;
    /**
     * JWT ID
     */
    private String jti;
    /**
     * Audience，观众
     */
    private String aud;


}
