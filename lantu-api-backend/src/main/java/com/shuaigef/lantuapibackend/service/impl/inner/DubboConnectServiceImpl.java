package com.shuaigef.lantuapibackend.service.impl.inner;

import com.shuaigef.lantuapicommon.service.DubboConnectService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * dubbo 连接测试服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@DubboService
public class DubboConnectServiceImpl implements DubboConnectService {

    @Override
    public String dubboConnectTest() {
        return "Dubbo 连接成功";
    }
}
