package com.shuaigef.lantuclientsdk.model.params;

import java.io.Serializable;
import lombok.Data;

/**
 * HelloWorld 接口请求参数
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Data
public class HelloWorldParams implements Serializable {

    private String text;

    private static final long serialVersionUID = 1;
}
