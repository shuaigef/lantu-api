package com.shuaigef.lantuapibackend.model.dto.interfaceInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 请求参数对象
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "请求参数对象")
@Data
public class RequestParamsField implements Serializable {

    /**
    * 参数名称
    */
    @ApiModelProperty("参数名称")
    @NotBlank(message = "名称不能为空")
    private String fieldName;

    /**
     * 名称
     */
    @ApiModelProperty("类型")
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
    * 描述
    */
    @ApiModelProperty("描述")
    private String description;

    private static final long serialVersionUID = 1L;

}
