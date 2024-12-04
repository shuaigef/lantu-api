package com.shuaigef.lantuapigateway.exception;

import com.shuaigef.lantuapicommon.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class GatewayException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public GatewayException(int code, String message) {
        super(message);
        this.code = code;
    }

    public GatewayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public GatewayException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
