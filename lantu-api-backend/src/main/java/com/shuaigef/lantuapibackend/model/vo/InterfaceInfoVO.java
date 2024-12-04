package com.shuaigef.lantuapibackend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.Field;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.RequestParamsField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 接口信息视图
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "接口信息视图")
@Data
public class InterfaceInfoVO implements Serializable {

    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Long id;

    /**
    * 名称
    */
    @ApiModelProperty("名称")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 接口头像
     */
    @ApiModelProperty("接口头像")
    private String interfaceAvatar;

    /**
    * 接口地址
    */
    @ApiModelProperty("接口地址")
    private String url;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private List<RequestParamsField> requestParams;

    /**
    * 请求头
    */
    @ApiModelProperty("请求头")
    private List<Field> requestHeader;

    /**
    * 响应头
    */
    @ApiModelProperty("响应头")
    private List<Field> responseHeader;

    /**
    * 接口状态（0-关闭，1-开启）
    */
    @ApiModelProperty("接口状态（0-关闭，1-开启）")
    private Integer status;

    /**
    * 请求类型
    */
    @ApiModelProperty("请求类型")
    private String method;

    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private Long userId;

    /**
     * 调用次数
     */
    @ApiModelProperty("调用次数")
    private Integer totalNum;

    /**
     * 消耗积分
     */
    @ApiModelProperty("消耗积分")
    private Integer consumePoints;

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

}
