package com.shuaigef.lantuclientsdk.model.request;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuaigef.lantuclientsdk.model.response.BaseResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求类
 *
 * @param <O> 请求参数类型
 * @param <T> 请求响应类型
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public abstract class BaseRequest<O, T extends BaseResponse> {

    private Map<String, Object> requestParams = new HashMap<>();

    public abstract String getMethod();

    public abstract String getPath();

    public abstract Class<T> getResponseClass();

    @JsonAnyGetter
    public Map<String, Object> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(O params) {
        this.requestParams = new Gson().fromJson(JSONUtil.toJsonStr(params), new TypeToken<Map<String, Object>>() {}.getType());
    }

}
