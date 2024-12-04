package com.shuaigef.lantuclientsdk.model.request;

import com.shuaigef.lantuapicommon.model.enums.RequestMethodEnum;
import com.shuaigef.lantuclientsdk.model.params.HelloWorldParams;
import com.shuaigef.lantuclientsdk.model.response.HelloWorldResponse;

/**
 * HelloWorld 接口请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class HelloWorldRequest extends BaseRequest<HelloWorldParams, HelloWorldResponse> {

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();

    }

    @Override
    public String getPath() {
        return "/api/helloworld";
    }

    @Override
    public Class<HelloWorldResponse> getResponseClass() {
        return HelloWorldResponse.class;
    }
}
