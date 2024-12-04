package com.shuaigef.lantuapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
* 接口信息表
*/
@Data
public class InterfaceInfo implements Serializable {

    /**
    * 主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 名称
    */
    private String name;

    /**
    * 描述
    */
    private String description;

    /**
     * 接口头像
     */
    private String interfaceAvatar;

    /**
    * 接口地址
    */
    private String url;

    /**
     * 请求参数
     *
     * [
     *     {"name": "username", "type": "string"}
     * ]
     */
    private String requestParams;

    /**
    * 请求头
    */
    private String requestHeader;

    /**
    * 响应头
    */
    private String responseHeader;

    /**
    * 接口状态（0-关闭，1-开启）
    */
    private Integer status;

    /**
    * 请求类型
    */
    private String method;

    /**
    * 创建人
    */
    private Long userId;

    /**
     * 消耗积分
     */
    private Integer consumePoints;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    private LocalDateTime updateTime;

    /**
    * 是否删除
    */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
