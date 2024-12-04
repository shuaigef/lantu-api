package com.shuaigef.lantuapibackend.model.dto.authority;

import com.shuaigef.lantuapibackend.common.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 权限分页查询请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "权限分页查询请求")
@Data
public class AuthorityQueryRequest extends PageRequest implements Serializable {

    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "权限名")
    private String name;

    @ApiModelProperty(value = "权限类型")
    private String authorityType;

    private static final long serialVersionUID = 1L;
}
