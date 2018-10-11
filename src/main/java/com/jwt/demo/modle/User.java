package com.jwt.demo.modle;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mr丶s
 * @ClassName User
 * @Version V1.0
 * @Date 2018/10/11 10:07
 * @Description
 */
@Data
@ApiModel(value = "用户模块")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ApiModelProperty(value = "主键id")
    Long id;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty(value = "密码")
    String password;

    @ApiModelProperty(value = "表创建时间")
    Data gmtCreated;

    @ApiModelProperty(value = "表修改时间")
    Data gmtUpdated;

    @ApiModelProperty(value = "验证码")
    String captcha;

    /**
     * 服务端验证码
     */
    private Object serviceVCode;

    /**
     * 是否需要验证码验证
     */
    boolean captchaValidate = true;

    /**
     * json
     *
     * @return json字符串
     */
    public String toJson() {
        return JSON.toJSONString(this);
    }

}
