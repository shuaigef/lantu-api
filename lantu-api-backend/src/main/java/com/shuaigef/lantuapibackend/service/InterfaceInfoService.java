package com.shuaigef.lantuapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.shuaigef.lantuapibackend.model.vo.InterfaceInfoVO;
import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;
import java.util.List;

/**
 * 接口服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口查询请求
     * @return
     */
    Page<InterfaceInfoVO> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 根据 id 获取接口
     *
     * @param id 接口id
     * @return 接口视图
     */
    InterfaceInfoVO getInterfaceInfoById(Long id);

    /**
     * 新增接口
     *
     * @param interfaceInfoAddRequest 接口新增请求
     * @return 是否成功
     */
    boolean addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest);

    /**
     * 修改接口
     *
     * @param interfaceInfoUpdateRequest 接口修改请求
     * @return 是否成功
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     * 删除接口
     *
     * @param id 接口id
     * @return 是否成功
     */
    boolean deleteInterfaceInfo(Long id);

    /**
     * 批量删除接口
     *
     * @param ids 接口id
     * @return 是否成功
     */
    boolean deleteBatchInterfaceInfo(List<Long> ids);

    /**
     * 接口信息脱敏
     *
     * @param interfaceInfo
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    /**
     * 接口信息脱敏
     *
     * @param interfaceInfoList
     * @return
     */
    List<InterfaceInfoVO> getInterfaceInfoVO(List<InterfaceInfo> interfaceInfoList);

}
