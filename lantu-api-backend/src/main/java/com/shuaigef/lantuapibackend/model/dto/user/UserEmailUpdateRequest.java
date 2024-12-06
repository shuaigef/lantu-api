package com.shuaigef.lantuapibackend.model.dto.user;

import com.shuaigef.lantuapibackend.common.utils.RegexUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户邮箱修改请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "用户邮箱修改请求")
@Data
public class UserEmailUpdateRequest implements Serializable {

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexUtils.EMAIL_REGEX, message = "邮箱错误")
    private String email;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    private static final long serialVersionUID = 1L;

}
