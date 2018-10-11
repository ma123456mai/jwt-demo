package com.jwt.demo.controller;
import com.jwt.demo.config.LoginConfig;
import com.jwt.demo.enums.ErrorEnum;
import com.jwt.demo.manager.LoginManager;
import com.jwt.demo.result.Result;
import com.jwt.demo.search.UserSearch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr丶s
 * @ClassName JwtController
 * @Version V1.0
 * @Date 2018/10/11 10:32
 * @Description
 */
@Slf4j
@Api(tags = "JWT")
@RestController
@RequestMapping
public class JwtController {


    @Autowired
    LoginManager loginManager;

    @Autowired
    LoginConfig loginConfig;


    @SuppressWarnings("unchecked")
    @PostMapping("/getValidateCode")
    @ApiOperation(value = "获取验证码(纯数字)")
    public Result getValidateCode() {
        try {
            String code = loginManager.createValidateCode();
            return Result.success().setEntry(code);
        } catch (Throwable e) {
            log.error("获取验证码异常", e);
            return Result.fail(e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/login")
    @ApiOperation(value = "JWT登录验证")
    public Result login(@RequestBody UserSearch search) {
        try {
            return loginManager.loginOfJTW(search);
        } catch (Throwable e) {
            log.error("登录操作异常", e);
            return Result.fail("登录操作异常",  ErrorEnum.DEFAULT_ERROR.getValue());
        }
    }

    @GetMapping("/getLoginUser")
    @ApiOperation(value = "获取登录用户信息")
    public Result getLoginUser(HttpServletRequest request) {
        return loginManager.getLoginUser(request);
    }




}
