package com.shuaigef.lantuclientsdk.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shuaigef.lantuclientsdk.client.LantuApiClient;
import com.shuaigef.lantuclientsdk.exception.ApiException;
import com.shuaigef.lantuclientsdk.exception.ErrorCode;
import com.shuaigef.lantuclientsdk.model.request.BaseRequest;
import com.shuaigef.lantuclientsdk.model.response.BaseResponse;
import com.shuaigef.lantuclientsdk.model.response.ErrorResponse;
import com.shuaigef.lantuclientsdk.utils.SignUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 通用服务抽象类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Slf4j
@Data
public abstract class BaseService implements ApiService {

    private LantuApiClient lantuApiClient;

    private String gatewayHost = "http://localhost:8088";

    public void checkConfig(LantuApiClient lantuApiClient) {
        if (lantuApiClient == null && this.lantuApiClient == null) {
            throw new ApiException(ErrorCode.FORBIDDEN_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        if (lantuApiClient != null && !StringUtils.isAnyBlank(lantuApiClient.getAccessKey(), lantuApiClient.getSecretKey())) {
            this.lantuApiClient = lantuApiClient;
        }
    }

    private <O, T extends BaseResponse> HttpResponse doRequest(BaseRequest<O, T> request) {
        try (HttpResponse httpResponse = getHttpRequestByRequestMethod(request).execute()) {
            return httpResponse;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    private <O, T extends BaseResponse> HttpRequest getHttpRequestByRequestMethod(BaseRequest<O, T> request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new ApiException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        String path = request.getPath();
        String method = request.getMethod();

        if (ObjectUtils.isEmpty(method)) {
            throw new ApiException(ErrorCode.PARAMS_ERROR, "请求方法不存在");
        }
        if (StringUtils.isBlank(path)) {
            throw new ApiException(ErrorCode.PARAMS_ERROR, "请求路径不存在");
        }

        if (path.startsWith(gatewayHost)) {
            path = path.substring(gatewayHost.length());
        }
        log.info("请求方法：{}，请求路径：{}，请求参数：{}", method, path, request.getRequestParams());
        HttpRequest httpRequest;
        switch (method) {
            case "GET": {
                httpRequest = HttpRequest.get(this.splicingGetRequest(request, path));
                break;
            }
            case "POST": {
                httpRequest = HttpRequest.post(gatewayHost + path);
                break;
            }
            case "PUT": {
                httpRequest = HttpRequest.put(gatewayHost + path);
                break;
            }
            case "DELETE": {
                httpRequest = HttpRequest.delete(gatewayHost + path);
                break;
            }
            default: {
                throw new ApiException(ErrorCode.OPERATION_ERROR, "不支持该请求");
            }
        }
        return httpRequest.addHeaders(getHeaderMap(JSONUtil.toJsonStr(request), lantuApiClient))
                .body(JSONUtil.toJsonStr(request.getRequestParams()));
    }

    public <O, T extends BaseResponse> T getResponse(BaseRequest<O, T> request) {
        if (this.lantuApiClient == null || StringUtils.isAnyBlank(this.lantuApiClient.getAccessKey(), this.lantuApiClient.getSecretKey())) {
            throw new ApiException(ErrorCode.FORBIDDEN_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        T rsp;
        try {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        HttpResponse httpResponse = doRequest(request);
        String body = httpResponse.body();
        Map<String, Object> data = new HashMap<>();
        if (httpResponse.getStatus() != 200) {
            ErrorResponse errorResponse = JSONUtil.toBean(body, ErrorResponse.class);
            data.put("errorMessage", errorResponse.getMessage());
            data.put("code", errorResponse.getCode());
        } else {
            try {
                // 尝试解析为JSON对象
                data = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {}.getType());
            } catch (JsonSyntaxException e) {
                // 解析失败，将body作为普通字符串处理
                data.put("value", body);
            }
        }
        rsp.setData(data);
        return rsp;
    }

    private <O, T extends BaseResponse> String splicingGetRequest(BaseRequest<O, T> request, String path) {
        StringBuilder urlBuilder = new StringBuilder(gatewayHost);
        // urlBuilder最后是/结尾且path以/开头的情况下，去掉urlBuilder结尾的/
        if (urlBuilder.toString().endsWith("/") && path.startsWith("/")) {
            urlBuilder.setLength(urlBuilder.length() - 1);
        }
        urlBuilder.append(path);
        if (!request.getRequestParams().isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, Object> entry : request.getRequestParams().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        log.info("GET请求路径：{}", urlBuilder);
        return urlBuilder.toString();
    }

    /**
     * 获取请求头
     *
     * @param body
     * @param lantuApiClient
     * @return
     */
    private Map<String, String> getHeaderMap(String body, LantuApiClient lantuApiClient) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", lantuApiClient.getAccessKey());
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        headerMap.put("body", body);
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("sign", SignUtils.genSign(body, lantuApiClient.getSecretKey()));
        return headerMap;
    }

    @Override
    public <O, T extends BaseResponse> T request(BaseRequest<O, T> request) {
        try {
            return getResponse(request);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    @Override
    public <O, T extends BaseResponse> T request(LantuApiClient lantuApiClient, BaseRequest<O, T> request) {
        checkConfig(lantuApiClient);
        return request(request);
    }


}
