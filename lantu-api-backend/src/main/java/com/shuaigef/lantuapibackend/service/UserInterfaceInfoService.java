package com.shuaigef.lantuapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuaigef.lantuapicommon.model.entity.UserInterfaceInfo;

/**
 * 用户接口服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 获取接口总调用次数
     *
     * @param interfaceId 接口id
     * @return 调用次数
     */
    int getTotalNumById(long interfaceId);

    /**
     * 调用接口
     *
     * @param interfaceInfoId 接口id
     * @param userId 用户id
     * @param reducePoints 扣除的积分
     * @return 是否成功
     */
    boolean invoke(long interfaceInfoId, long userId, int reducePoints);

}
