package com.shuaigef.lantuclientsdk.model.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * 通用返回类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@NoArgsConstructor
public class BaseResponse implements Serializable {

    private Map<String, Object> data = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    private static final long serialVersionUID = 1L;

}
