package com.shuaigef.lantuclientsdk.exception;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class ApiException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
