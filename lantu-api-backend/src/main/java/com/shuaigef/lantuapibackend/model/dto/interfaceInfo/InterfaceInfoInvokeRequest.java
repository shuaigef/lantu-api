package com.shuaigef.lantuapibackend.model.dto.interfaceInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 接口调用请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "接口调用请求")
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
    * 主键
    */
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private List<Field> requestParams;

    private static final long serialVersionUID = 1L;

}
