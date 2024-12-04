package com.shuaigef.lantuapibackend.model.dto.interfaceInfo;

import com.shuaigef.lantuapibackend.common.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接口信息查询请求
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@ApiModel(description = "接口信息查询请求")
@Data
public class InterfaceInfoQueryRequest extends PageRequest {

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String name;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    @ApiModelProperty("接口状态（0-关闭，1-开启）")
    private Integer status;



}
