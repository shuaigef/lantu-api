package com.shuaigef.lantuclientsdk.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 调用第三方接口的客户端
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LantuApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8088";

    private String accessKey;

    private String secretKey;

}

