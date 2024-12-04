package com.shuaigef.lantuapibackend;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shuaigef.lantuapibackend.service.InterfaceInfoService;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    void mockData() {
        String requestParams = "[{\"fieldName\":\"username\", \"type\":\"string\"}]";
        String requestHeader = "[{\"fieldName\":\"Content-Type\", \"type\":\"application/json\"}]";
        String responseHeader = "[{\"fieldName\":\"Content-Type\", \"type\":\"application/json\"}]";

        interfaceInfoService.update(new LambdaUpdateWrapper<InterfaceInfo>()
                .set(InterfaceInfo::getRequestHeader, "[]")
                .set(InterfaceInfo::getResponseHeader, "[]")
        );
    }

    @Test
    void sendMail() {
        MailUtil.send("3159775306@qq.com", "测试", "邮件来自Hutool测试", false);
    }

}
