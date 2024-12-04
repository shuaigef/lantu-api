package com.shuaigef.lantuapibackend.model.dto.user;

import com.shuaigef.lantuapibackend.common.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户新增请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "分页查询用户请求")
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    private static final long serialVersionUID = 1L;
}
