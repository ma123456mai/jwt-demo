package com.jwt.demo.dto;


import com.jwt.demo.modle.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author   Mr丶s
 * @ClassName
 * @Version   V1.0
 * @Date   2018/10/9 21:49
 * @Description
 */
@Data
@AllArgsConstructor
public class JWTContext {

    /**
     * 用户
     */
    private User user;

    /**
     * JWT id
     */
    private String jti;

    /**
     * 过期时间
     */
    private Date expiresAt;

    public JWTContext(User user) {
        this.user = user;
    }
}
