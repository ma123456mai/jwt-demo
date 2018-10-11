package com.jwt.demo.enums;

/**
 * @author   错误类型
 * @ClassName   Mr丶s
 * @Version   V1.0
 * @Date   2018/10/11 10:44
 * @Description
 */
public enum ErrorEnum {
    /**
     * 常见错误
     */
    ID_NULL(99, "id为空"),
    DEFAULT_ERROR(100, "默认错误"),
    PARAMS_ERROR(101, "参数错误"),
    HAS_NO_POWER(102, "没有权限操作"),
    DB_ERROR(103, "数据库操作错误"),
    DATA_NOT_EXIST(104, "操作数据不存在"),
    PARAM_NULL(105, "参数为空"),
    LOGIN_VALIDATE_CODE_ERROR(106, "验证码已经过期或者填写错误"),
    ADD_DATA_NULL(107, "添加数据为空"),
    DATA_REPEAT(108, "数据重复"),
    DATA_DELETE_COMPLETED(109, "数据已被删除"),
    FILE_EXISTS(110, "文件已存在"),
    USER_LOGINED_ANOTHER_PLACE(112, "账号已经在另一处登录"),

    /**
     * 网络相关
     */
    NET_TIMEOUT_ERROR(600, "网络超时"),
    NET_ERROR(601, "网络异常"),
    JWT_HAS_EXPIRED(700, "Token过期"),
    JWT_VALIDATE_FAIL(701, "JWT验证失败"),


    /**
     * 用户相关
     */
    NOT_LOGIN(10212, "用户未登录"),
    USER_WRITE_OFF(10213, "用户已经注销"),
    USER_NOT_EXIT(500, "用户不存在"),
    USER_PASSWORD_ERROR(501, "密码错误"),
    USER_ORIGINAL_ERROR(502,"原密码错误"),
    USER_NAME_NULL(10214, "用户名为空(手机号)"),
    USER_PASSWORD_NULL(10215, "用户密码为空"),
    USER_TYPE_NULL(10216, "用户类型为空"),
    USER_SEX_NULL(10217, "用户性别为空"),
    USER_NAME_EXIST(10218, "用户名(手机号)已经存在"),
    SING_VERIFY_ERROR(10219, "签名验证失败"),
    NO_GET_LOGIN_USER(10220, "没有获取到登录用户");

    /**
     * 值
     */
    private Integer value;

    /**
     * 名称
     */
    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ErrorEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ErrorEnum getByValue(Integer value) {
        if (null == value) {
            return null;
        }
        for (ErrorEnum errorEnum : ErrorEnum.values()) {
            if (errorEnum.getValue().equals(value)) {
                return errorEnum;
            }
        }
        return null;
    }
}
