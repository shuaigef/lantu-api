package com.shuaigef.lantuapibackend.model.dto.user;

import com.shuaigef.lantuapibackend.common.utils.RegexUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户新增请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "用户新增请求")
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 10, message = "用户名长度必须为 4 - 10 位")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 4, max = 10, message = "密码长度必须为 6 - 16 位")
    private String password;

    /**
     * 密码
     */
    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 4, max = 10, message = "密码长度必须为 6 - 16 位")
    private String checkPassword;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 10, message = "昵称长度必须为 2 - 10 位")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    /**
     * 用户简介
     */
    @ApiModelProperty(value = "用户简介")
    @Size(max = 50, message = "用户简介不能超过50个字符")
    private String userProfile;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    @Min(value = 1, message = "角色id 不能小于 1")
    private Long roleId;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexUtils.EMAIL_REGEX, message = "邮箱错误")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}$", message = "手机号错误")
    private String phoneNumber;

    private static final long serialVersionUID = 1L;
}
