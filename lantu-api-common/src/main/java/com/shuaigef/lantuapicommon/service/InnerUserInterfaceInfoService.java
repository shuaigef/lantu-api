package com.shuaigef.lantuapicommon.service;

/**
 * 用户接口关系服务
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId, int reducePoints);

}
