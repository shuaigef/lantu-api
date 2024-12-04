package com.shuaigef.lantuapigateway;

import com.shuaigef.lantuapicommon.service.DubboConnectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
@Service
@Slf4j
public class MainApplication {

    @DubboReference
    private DubboConnectService dubboConnectService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        MainApplication application = context.getBean(MainApplication.class);
        log.info("【Dubbo服务】：{}", application.dubboConnectTest());

    }

    public String dubboConnectTest() {
        return dubboConnectService.dubboConnectTest();
    }

}

