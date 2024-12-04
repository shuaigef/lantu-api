package com.shuaigef.lantuapicommon.common;

/**
 * 自定义成功码
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public enum SuccessCode {

    SUCCESS(0, "操作成功");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
