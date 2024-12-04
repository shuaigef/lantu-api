package com.shuaigef.lantuclientsdk.service;

import com.shuaigef.lantuclientsdk.client.LantuApiClient;
import com.shuaigef.lantuclientsdk.model.request.BaseRequest;
import com.shuaigef.lantuclientsdk.model.request.HelloWorldRequest;
import com.shuaigef.lantuclientsdk.model.request.IpInfoRequest;
import com.shuaigef.lantuclientsdk.model.response.BaseResponse;

/**
 * api 服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface ApiService {

    <O, T extends BaseResponse> T request(BaseRequest<O, T> request);

    <O, T extends BaseResponse> T request(LantuApiClient lantuApiClient, BaseRequest<O, T> request);

    BaseResponse getIpInfo(LantuApiClient lantuApiClient, IpInfoRequest request);

    BaseResponse helloWorld(LantuApiClient lantuApiClient, HelloWorldRequest request);


}
