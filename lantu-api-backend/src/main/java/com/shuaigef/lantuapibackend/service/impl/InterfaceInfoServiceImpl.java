package com.shuaigef.lantuapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.shuaigef.lantuapibackend.common.utils.SecurityUtils;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.mapper.InterfaceInfoMapper;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.Field;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.RequestParamsField;
import com.shuaigef.lantuapibackend.model.enums.InterfaceInfoStatusEnum;
import com.shuaigef.lantuapibackend.model.enums.RequestMethodEnum;
import com.shuaigef.lantuapibackend.model.vo.InterfaceInfoVO;
import com.shuaigef.lantuapibackend.service.InterfaceInfoService;
import com.shuaigef.lantuapibackend.service.UserInterfaceInfoService;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 接口服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    private final Gson gson = new Gson();

    @Override
    public Page<InterfaceInfoVO> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        String name = interfaceInfoQueryRequest.getName();
        Integer status = interfaceInfoQueryRequest.getStatus();
        InterfaceInfoStatusEnum interfaceInfoStatusEnum = InterfaceInfoStatusEnum.getEnumByValue(status);
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        LambdaQueryWrapper<InterfaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(name), InterfaceInfo::getName, name)
                .eq(interfaceInfoStatusEnum != null, InterfaceInfo::getStatus, status);
        Page<InterfaceInfo> interfaceInfoPage = this.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(current, pageSize, interfaceInfoPage.getTotal());
        List<InterfaceInfoVO> interfaceInfoVOList = this.getInterfaceInfoVO(interfaceInfoPage.getRecords());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoById(Long id) {
        if (id == null || id < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id 错误");
        }
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该接口不存在");
        }
        return this.getInterfaceInfoVO(interfaceInfo);
    }

    @Override
    public boolean addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        validateInterface(interfaceInfo, false);

        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        interfaceInfo.setUserId(SecurityUtils.getCurrentUserId());
        interfaceInfo.setRequestParams(gson.toJson(interfaceInfoAddRequest.getRequestParams()));
        interfaceInfo.setRequestHeader(gson.toJson(interfaceInfoAddRequest.getRequestHeader()));
        interfaceInfo.setResponseHeader(gson.toJson(interfaceInfoAddRequest.getResponseHeader()));
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增失败");
        }
        return result;
    }

    private String formatField(List<Field> fieldList) {
        String requestParams = "{}";
        if (fieldList != null && fieldList.size() > 0) {
            JsonObject jsonObject = new JsonObject();
            for (Field field : fieldList) {
                jsonObject.addProperty(field.getFieldName(), field.getValue());
            }
            requestParams = gson.toJson(jsonObject);
        }
        return requestParams;
    }

    @Override
    public boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        validateInterface(interfaceInfo, true);

        interfaceInfo.setRequestParams(gson.toJson(interfaceInfoUpdateRequest.getRequestParams()));
        interfaceInfo.setRequestHeader(gson.toJson(interfaceInfoUpdateRequest.getRequestHeader()));
        interfaceInfo.setResponseHeader(gson.toJson(interfaceInfoUpdateRequest.getResponseHeader()));
        boolean result = this.updateById(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return result;
    }

    @Override
    public boolean deleteInterfaceInfo(Long id) {
        InterfaceInfo selectOne = this.getById(id);
        if (selectOne == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口信息不存在");
        }
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return result;
    }

    @Override
    public boolean deleteBatchInterfaceInfo(List<Long> ids) {
        boolean result = this
                .remove(new LambdaQueryWrapper<InterfaceInfo>().in(InterfaceInfo::getId, ids));
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return result;
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        interfaceInfoVO.setRequestParams(gson.fromJson(interfaceInfo.getRequestParams(), new TypeToken<List<RequestParamsField>>() {}.getType()));
        interfaceInfoVO.setRequestHeader(gson.fromJson(interfaceInfo.getRequestHeader(), new TypeToken<List<Field>>() {}.getType()));
        interfaceInfoVO.setResponseHeader(gson.fromJson(interfaceInfo.getResponseHeader(), new TypeToken<List<Field>>() {}.getType()));
        interfaceInfoVO.setTotalNum(userInterfaceInfoService.getTotalNumById(interfaceInfo.getId()));
        return interfaceInfoVO;
    }

    @Override
    public List<InterfaceInfoVO> getInterfaceInfoVO(List<InterfaceInfo> interfaceInfoList) {
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            return new ArrayList<>();
        }
        return interfaceInfoList.stream().map(this::getInterfaceInfoVO).collect(Collectors.toList());
    }

    /**
     * 接口字段校验
     *
     * @param interfaceInfo 接口信息
     * @param isUpdate 是否为更新用户，如果更新则排除 id == interfaceInfo.getId() 的记录
     */
    private void validateInterface(InterfaceInfo interfaceInfo, boolean isUpdate) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 接口不存在
        Long id = interfaceInfo.getId();
        if (isUpdate && this.getById(id) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
        }

        // 校验接口名称
        String name = interfaceInfo.getName();
        if (StringUtils.isNotBlank(name)) {
            validateUniqueField(InterfaceInfo::getName, name, id, isUpdate, "接口名称重复");
        }

        // 校验请求方法
        String method = interfaceInfo.getMethod();
        if (StringUtils.isNotBlank(method) && RequestMethodEnum.getEnumByValue(method) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求方法不存在");
        }
    }

    /**
     * 校验字段唯一性
     *
     * @param column   需要校验的字段（Lambda表达式）
     * @param value    字段值
     * @param id       当前记录ID
     * @param isUpdate 是否为更新操作
     * @param errorMsg 异常提示信息
     */
    private void validateUniqueField(SFunction<InterfaceInfo, ?> column, Object value, Long id, boolean isUpdate, String errorMsg) {
        InterfaceInfo existingRole = this.getOne(new LambdaQueryWrapper<InterfaceInfo>()
                .eq(column, value)
                .ne(isUpdate && id != null, InterfaceInfo::getId, id));
        if (existingRole != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, errorMsg);
        }
    }

}




