package com.shuaigef.lantuapicommon.service;

import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;

/**
* 接口服务
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

}
