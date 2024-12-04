package com.shuaigef.lantuapibackend.model.dto.interfaceInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 接口信息修改请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "接口信息修改请求")
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
    * 主键
    */
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    /**
    * 名称
    */
    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty("描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 接口头像
     */
    @ApiModelProperty("接口头像")
    private String interfaceAvatar;

    /**
     * 消耗积分
     */
    @ApiModelProperty("消耗积分")
    @Min(value = 0, message = "消耗积分不能小于 0")
    private Integer consumePoints;

    /**
    * 接口地址
    */
    @ApiModelProperty("接口地址")
    @NotBlank(message = "接口地址不能为空")
    private String url;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    @Valid
    private List<RequestParamsField> requestParams;

    /**
    * 请求头
    */
    @ApiModelProperty("请求头")
    @Valid
    private List<Field> requestHeader;

    /**
    * 响应头
    */
    @ApiModelProperty("响应头")
    @Valid
    private List<Field> responseHeader;

    /**
    * 请求类型
    */
    @ApiModelProperty("请求类型")
    @NotBlank(message = "请求类型不能为空")
    private String method;

    private static final long serialVersionUID = 1L;

}
