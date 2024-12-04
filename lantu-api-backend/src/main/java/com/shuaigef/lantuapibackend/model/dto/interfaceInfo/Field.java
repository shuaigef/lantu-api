package com.shuaigef.lantuapibackend.model.dto.interfaceInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字段类
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "字段类")
@Data
public class Field implements Serializable {

    /**
    * 字段名
    */
    @ApiModelProperty("字段名")
    @NotBlank(message = "字段名不能为空")
    private String fieldName;

    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    @NotBlank(message = "字段值不能为空")
    private String value;

    private static final long serialVersionUID = 1L;

}
