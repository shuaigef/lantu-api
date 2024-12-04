package com.shuaigef.lantuclientsdk.service.impl;

import com.shuaigef.lantuclientsdk.client.LantuApiClient;
import com.shuaigef.lantuclientsdk.model.request.HelloWorldRequest;
import com.shuaigef.lantuclientsdk.model.request.IpInfoRequest;
import com.shuaigef.lantuclientsdk.model.response.BaseResponse;
import com.shuaigef.lantuclientsdk.service.ApiService;
import com.shuaigef.lantuclientsdk.service.BaseService;

/**
 * api 服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class ApiServiceImpl extends BaseService implements ApiService {

    @Override
    public BaseResponse getIpInfo(LantuApiClient lantuApiClient, IpInfoRequest request) {
        return request(lantuApiClient, request);
    }

    @Override
    public BaseResponse helloWorld(LantuApiClient lantuApiClient, HelloWorldRequest request) {
        return request(lantuApiClient, request);
    }
}
