package com.jwt.demo.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jwt.demo.dto.JWTContext;
import com.jwt.demo.manager.LoginManager;
import com.jwt.demo.modle.User;
import com.jwt.demo.pojo.CoronPayload;
import com.jwt.demo.result.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  JWTUtil
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/9 21:47
 * @Description   
 */
@Slf4j
@Data
@Component
public class JWTUtil {

    @Autowired
    private LoginManager manager;

    //配置注入
    private static LoginManager loginManager;

    @PostConstruct
    public void init(){
        loginManager = manager;
    }

    /**
     * JWT Token 密钥
     */
    private static String JWT_SECRET = "jwt_2018";
    /**
     * 发行者
     */
    private static String ISSUER = "springboot-jwt";

    /**
     * JWT Token 生成器
     *
     * @param context 上下文
     * @return Token
     */
    public static String createToken(JWTContext context) {
        User user = context.getUser();
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTCreator.Builder builder = JWT.create();

            Map<String, Object> heads = createHeaderClaims(context);
            addPayload(user, builder, context);

            String token = builder.withHeader(heads).sign(algorithm);
            return token;
        } catch (Exception e) {
            log.error("JWT 生成Token 失败，user = " + JSON.toJSONString(user), e);
            throw new RuntimeException("JWT 生成Token 失败");
        }
    }


    /**
     * 生成Header信息
     *
     * @param context 参数上下文
     * @return Header
     */
    private static Map<String, Object> createHeaderClaims(JWTContext context) {
        Map<String, Object> heads = new HashMap<>();
        heads.put(PublicClaims.TYPE, "JWT");
        heads.put(PublicClaims.ALGORITHM, "HS256");
        return heads;
    }

    /**
     * 创建Payload
     *
     * @param user 登录用户
     * @return
     */
    private static void addPayload(User user, JWTCreator.Builder builder, JWTContext context) {
        String jti = StringUtil.uniqueKey();
        builder.withJWTId(jti);
        context.setJti(jti);

        builder.withClaim("id", user.getId());//自定义，用户id
        builder.withIssuer(ISSUER);
        builder.withIssuedAt(new Date());//发行时间
        builder.withExpiresAt(context.getExpiresAt());
    }

    /**
     * 校验
     *
     * @param token 待校验token
     * @return DecodedJWT 值
     */
    public static Result<CoronPayload> verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            String payloadEncStr = jwt.getPayload();
            String payload = new String(Base64.decodeBase64(payloadEncStr));

            CoronPayload coronPayload = JSON.parseObject(payload, CoronPayload.class);
            return Result.success(coronPayload);
        } catch (JWTVerificationException exception) {
            log.error("Invalid signature/claims", exception);
            return Result.fail("Invalid signature/claims:" + exception.getMessage());
        }
    }

    /**
     * 检测用户Token是否已经更新，验证用户绑定的Token和当前Token是否一致
     * true-已经更新，false-未更新
     *
     * @param coronPayload 用户信息
     * @param token        当前传入Token
     * @return 检测结果
     */
    public static boolean validateUserTokenAlreadyUpdated(CoronPayload coronPayload, String token) {
        if (null == coronPayload || StringUtil.isNullStr(token)) {
            return true;
        }
        String uid = coronPayload.getUid();
        if (StringUtil.isNullStr(uid)) {
            return true;
        }
        String redisToken = loginManager.getUserToken(uid);

        if (StringUtil.isNullStr(redisToken)) {//如果存储的token为null或者空字符串则认为未被踢掉，校验交给后续
            return false;
        }
        return !token.equals(redisToken);
    }

}
