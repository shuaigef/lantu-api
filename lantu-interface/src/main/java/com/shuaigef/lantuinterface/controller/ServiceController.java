package com.shuaigef.lantuinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuaigef.lantuclientsdk.model.params.HelloWorldParams;
import com.shuaigef.lantuclientsdk.model.params.IpInfoParams;
import com.shuaigef.lantuclientsdk.model.response.BaseResponse;
import com.shuaigef.lantuclientsdk.model.response.HelloWorldResponse;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ServiceController {

    @GetMapping("/ipInfo")
    public BaseResponse getIpInfo(IpInfoParams ipInfoParams) {
        Map<String, Object> paramsMap = new HashMap<>(BeanMap.create(ipInfoParams));

        String response = HttpUtil.get("https://qifu-api.baidubce.com/ip/local/geo/v1/district", paramsMap);
        Map<String, Object> fromResponse = new Gson().fromJson(response,
                new TypeToken<Map<String, Object>>() {}.getType());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(fromResponse);
        return baseResponse;
    }

    @GetMapping("/helloworld")
    public HelloWorldResponse helloWorld(HelloWorldParams helloWorldParams) {
        StringBuilder result = new StringBuilder("Hello World");
        String text = helloWorldParams.getText();
        if (StringUtils.isNotBlank(text)) {
            result.append(" " + text);
        }
        helloWorldParams.setText(result.toString());
        return JSONUtil.toBean(JSONUtil.toJsonStr(helloWorldParams), HelloWorldResponse.class);
    }

}
