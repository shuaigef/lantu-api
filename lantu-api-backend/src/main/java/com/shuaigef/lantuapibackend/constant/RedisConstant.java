package com.shuaigef.lantuapibackend.constant;

import java.util.concurrent.TimeUnit;

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
     * 邮箱验证码有效时间 5分钟
     */
    Long VERIFICATION_CODE_TIME = 5l;
    TimeUnit VERIFICATION_CODE_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 验证码发送间隔 key
     */
    String INTERVAL_KEY = "interval_key:";
    /**
     * 验证码发送间隔时间 1分钟
     */
    Long INTERVAL_KEY_TIME = 1l;
    TimeUnit INTERVAL_KEY_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 用户登录 key
     */
    String LOGIN_USER = "login_user:";
    /**
     * 用户登录有效期 1天
     */
    Long LOGIN_USER_TIME = 1l;
    TimeUnit LOGIN_USER_TIME_UNIT = TimeUnit.DAYS;

}
