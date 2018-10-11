package com.jwt.demo.result;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author   返回转台
 * @ClassName  Mr丶s
 * @Version   V1.0
 * @Date   2018/10/11 10:36
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "返回结果")
public class Result<T> {

    @ApiModelProperty(value = "默认成功的code")
    private static final String DEFAULT_SUCCESS_CODE = "10000";

    @ApiModelProperty(value = "默认失败")
    private static final String DEFAULT_FAIL_CODE = "-1";

    @ApiModelProperty(value = "状态")
    private boolean status;

    @ApiModelProperty(value = "接口请求消息")
    private String message;

    @ApiModelProperty(value = "中文提示信息")
    private String cnMessage;

    @ApiModelProperty(value = "接口返回编码")
    private Object responseCode;

    @ApiModelProperty(value = "具体业务编码")
    private String subCode;

    @ApiModelProperty(value = "业务信息")
    private String subMsg;

    @ApiModelProperty(value = "结果对象")
    private T entry;

    /**
     * 具体异常类
     */
    private Exception e;


    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getEntry() {
        return entry;
    }

    public Result setEntry(T entry) {
        this.entry = entry;
        return this;
    }

    /**
     * 成功
     *
     * @return
     */
    public static Result success() {
        Result result = new Result();
        result.setStatus(true);
        result.setResponseCode(DEFAULT_SUCCESS_CODE);
        return result;
    }

    public static Result success(String msg) {
        Result result = success();
        result.setMessage(msg);
        result.setEntry(msg);
        return result;
    }

    public static Result success(String msg,String entry) {
        Result result = success();
        result.setMessage(msg);
        result.setEntry(entry);
        return result;
    }

    public static Result success(String msg, Object responseCode) {
        Result result = success(msg);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result success(Object entry) {
        Result result = success();
        result.setEntry(entry);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setStatus(false);
        result.setResponseCode(DEFAULT_FAIL_CODE);
        return result;
    }

    public static Result fail(String msg) {
        Result result = fail();
        result.setMessage(msg);
        return result;
    }

    public static Result fail(String msg,Exception e) {
        Result result = fail();
        result.setMessage(msg);
        result.setE(e);
        return result;
    }

    public static Result fail(String msg, Object responseCode) {
        Result result = fail(msg);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result fail(String msg, Object responseCode,Exception e) {
        Result result = fail(msg,e);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result fail(String cnMessage,String msg, Object responseCode) {
        Result result = fail(msg);
        result.setCnMessage(cnMessage);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result fail(Object entry) {
        Result result = fail();
        result.setEntry(entry);
        return result;
    }

    /**
     * json
     *
     * @return
     */
    public String toJson() {
        return JSON.toJSONString(this);
    }

}
