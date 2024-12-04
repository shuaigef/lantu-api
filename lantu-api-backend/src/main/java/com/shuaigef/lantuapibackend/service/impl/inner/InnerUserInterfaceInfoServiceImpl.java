package com.shuaigef.lantuapibackend.service.impl.inner;

import com.shuaigef.lantuapibackend.service.UserInterfaceInfoService;
import com.shuaigef.lantuapicommon.service.InnerUserInterfaceInfoService;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 远程调用用户接口服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean invokeCount(long interfaceInfoId, long userId, int reducePoints) {
        return userInterfaceInfoService.invoke(interfaceInfoId, userId, reducePoints);
    }

}
