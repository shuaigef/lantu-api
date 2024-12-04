package com.shuaigef.lantuapibackend.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户修改密码请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "用户修改密码请求")
@Data
public class UserUpdatePasswordRequest implements Serializable {

    /**
     * 原始密码
     */
    @ApiModelProperty(value = "原始密码", required = true)
    @NotBlank(message = "原始密码不能为空")
    @Size(min = 6, max = 20, message = "原始密码必须为6-20位")
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码必须为6-20位")
    private String newPassword;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 20, message = "密码必须为6-20位")
    private String checkPassword;

    private static final long serialVersionUID = 1L;

}
