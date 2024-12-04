package com.shuaigef.lantuclientsdk;

import com.shuaigef.lantuclientsdk.client.LantuApiClient;
import com.shuaigef.lantuclientsdk.service.ApiService;
import com.shuaigef.lantuclientsdk.service.impl.ApiServiceImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("lantu.client")
@Data
@ComponentScan
public class LantuClientSdkConfig {

    private String accessKey;

    private String secretKey;

    private String gatewayHost;

    @Bean
    public LantuApiClient lantuApiClient() {
        return new LantuApiClient(accessKey, secretKey);
    }

    @Bean
    public ApiService apiService() {
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setLantuApiClient(new LantuApiClient(accessKey, secretKey));
        if (StringUtils.isNotBlank(gatewayHost)) {
            apiService.setGatewayHost(gatewayHost);
        }
        return apiService;
    }

}
