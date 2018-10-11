package com.jwt.demo.exception;

import lombok.Data;

/**
 * @author  Mr丶s
 * @ClassName 业务异常类
 * @Version   V1.0
 * @Date   2018/10/10 18:01
 * @Description
 */
@Data
public class KinshipRuntimeException extends RuntimeException {

    /**
     * 异常编码
     */
    private Object code;
    /**
     * 中文提示语
     */
    private String cnMessage;


    public KinshipRuntimeException(String message) {
        super(message);
    }

    public KinshipRuntimeException(String message, Object code) {
        super(message);
        this.code = code;
    }

    public KinshipRuntimeException(String cnMessage, String message, Object code) {
        this(message, code);
        this.cnMessage = cnMessage;
    }
}
