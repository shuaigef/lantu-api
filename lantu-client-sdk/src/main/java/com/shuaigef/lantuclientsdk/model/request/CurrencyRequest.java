package com.shuaigef.lantuclientsdk.model.request;

import com.shuaigef.lantuclientsdk.model.response.BaseResponse;

/**
 * 通用请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public class CurrencyRequest extends BaseRequest<Object, BaseResponse> {

    private String path;

    private String method;

    @Override
    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Class<BaseResponse> getResponseClass() {
        return BaseResponse.class;
    }
}
