package com.shuaigef.lantuclientsdk.model.response;

import lombok.Data;

/**
 * 通用错误返回类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Data
public class ErrorResponse {

    private int code;

    private String message;

}
