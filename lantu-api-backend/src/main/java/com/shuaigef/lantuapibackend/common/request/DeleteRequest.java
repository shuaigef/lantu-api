package com.shuaigef.lantuapibackend.common.request;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id 必须大于 0")
    private Long id;

    private static final long serialVersionUID = 1L;
}
