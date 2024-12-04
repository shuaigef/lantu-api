package com.shuaigef.lantuclientsdk.model.request;

import com.shuaigef.lantuapicommon.model.enums.RequestMethodEnum;
import com.shuaigef.lantuclientsdk.model.params.IpInfoParams;
import com.shuaigef.lantuclientsdk.model.response.IpInfoResponse;

/**
 * IPInfo 接口请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class IpInfoRequest extends BaseRequest<IpInfoParams, IpInfoResponse> {

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }

    @Override
    public String getPath() {
        return "/api/ipInfo";
    }

    @Override
    public Class<IpInfoResponse> getResponseClass() {
        return IpInfoResponse.class;
    }
}
