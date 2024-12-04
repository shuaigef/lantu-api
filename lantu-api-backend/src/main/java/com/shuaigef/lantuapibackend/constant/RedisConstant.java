package com.shuaigef.lantuapibackend.constant;

/**
 * redis 常量
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface RedisConstant {

    /**
     * 邮箱验证码 key
     */
    String VERIFICATION_CODE_KEY = "verification_code_key:";

    /**
     * 邮箱验证码有效时间
     */
    Long VERIFICATION_CODE_TIME = 5l;

    /**
     * 验证码发送间隔 key
     */
    String INTERVAL_KEY = "interval_key:";

    /**
     * 验证码发送间隔时间
     */
    Long INTERVAL_KEY_TIME = 1l;

}
