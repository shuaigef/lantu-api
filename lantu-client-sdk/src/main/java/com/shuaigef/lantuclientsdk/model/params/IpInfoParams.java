package com.shuaigef.lantuclientsdk.model.params;

import java.io.Serializable;
import lombok.Data;

/**
 * IpInfo 接口请求参数
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Data
public class IpInfoParams implements Serializable {

    private String ip;

    private static final long serialVersionUID = 1;
}
