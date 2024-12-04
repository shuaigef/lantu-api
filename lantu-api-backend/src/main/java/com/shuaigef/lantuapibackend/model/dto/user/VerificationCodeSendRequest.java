package com.shuaigef.lantuapibackend.model.dto.user;

import com.shuaigef.lantuapibackend.common.utils.RegexUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 邮箱验证码发送请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "邮箱验证码发送请求")
@Data
public class VerificationCodeSendRequest implements Serializable {

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexUtils.EMAIL_REGEX, message = "邮箱错误")
    private String email;

    private static final long serialVersionUID = 1L;

}
