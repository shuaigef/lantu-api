package com.shuaigef.lantuapicommon.service;

import com.shuaigef.lantuapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface InnerUserService {

    /**
     * 数据库中查询是否已分配给用户密钥（accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
