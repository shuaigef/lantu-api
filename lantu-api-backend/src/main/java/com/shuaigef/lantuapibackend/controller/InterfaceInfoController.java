package com.shuaigef.lantuapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.shuaigef.lantuapibackend.common.request.DeleteBatchRequest;
import com.shuaigef.lantuapibackend.common.request.DeleteRequest;
import com.shuaigef.lantuapibackend.common.request.IdRequest;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.Field;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.shuaigef.lantuapibackend.model.enums.InterfaceInfoStatusEnum;
import com.shuaigef.lantuapibackend.model.vo.InterfaceInfoVO;
import com.shuaigef.lantuapibackend.service.InterfaceInfoService;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.common.BaseResponse;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.common.ResultUtils;
import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;
import com.shuaigef.lantuapicommon.model.entity.User;
import com.shuaigef.lantuclientsdk.client.LantuApiClient;
import com.shuaigef.lantuclientsdk.model.request.CurrencyRequest;
import com.shuaigef.lantuclientsdk.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口管理接口
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 **/
@Api(tags = "接口管理")
@RestController
@RequestMapping("/interface")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private ApiService apiService;

    @Resource
    private UserService userService;

    private final Gson gson = new Gson();

    @ApiOperation("分页查询接口信息")
    @GetMapping("/list")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        return ResultUtils.success(interfaceInfoService.listInterfaceInfoByPage(interfaceInfoQueryRequest), "查询接口信息成功");
    }

    @ApiOperation("根据 id 查询接口信息")
    @GetMapping("/id")
    public BaseResponse<InterfaceInfoVO> listInterfaceInfoById(Long id) {
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoById(id), "查询接口信息成功");
    }

    @ApiOperation("新增接口信息")
    @PostMapping
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> addInterfaceInfo(@Valid @RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        return ResultUtils.success(interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest), "新增接口信息成功");
    }

    @ApiOperation("修改接口信息")
    @PutMapping
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> updateInterfaceInfo(@Valid @RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        return ResultUtils.success(interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest), "修改接口信息成功");
    }

    @ApiOperation("删除接口信息")
    @DeleteMapping
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> deleteInterfaceInfo(@Valid @RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.success(interfaceInfoService.deleteInterfaceInfo(deleteRequest.getId()), "删除接口信息成功");
    }

    @ApiOperation("批量删除接口信息")
    @DeleteMapping("/ids")
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> deleteInterfaceInfo(@Valid @RequestBody DeleteBatchRequest deleteBatchRequest) {
        return ResultUtils.success(interfaceInfoService.deleteBatchInterfaceInfo(deleteBatchRequest.getIds()), "批量删除接口信息成功");
    }

    @ApiOperation("发布")
    @PutMapping("/online")
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> onlineInterfaceInfo(@Valid @RequestBody IdRequest idRequest, HttpServletRequest request) {
        Long id = idRequest.getId();
        // 校验该接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        // todo 接口是否可以调用

        // 修改状态
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result, "发布接口信息成功");
    }

    @ApiOperation("下线")
    @PutMapping("/offline")
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse<Boolean> offlineInterfaceInfo(@Valid @RequestBody IdRequest idRequest, HttpServletRequest request) {
        Long id = idRequest.getId();
        // 校验该接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        // 修改状态
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result, "关闭接口信息成功");
    }

    @ApiOperation("接口调用")
    @PostMapping("/invoke")
    @PreAuthorize("@roleCheckService.hasPermission('interfaceManage:index')")
    public BaseResponse invokeInterfaceInfo(@Valid @RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        Long id = interfaceInfoInvokeRequest.getId();
        List<Field> fieldList = interfaceInfoInvokeRequest.getRequestParams();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        // 校验
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 获取调用用户的accessKey和secretKey
        User currentUser = userService.getCurrentUser();
        String accessKey = currentUser.getAccessKey();
        String secretKey = currentUser.getSecretKey();
        // 构建参数 fieldList -> json -> map
        String requestParams = "{}";
        if (fieldList != null && fieldList.size() > 0) {
            JsonObject jsonObject = new JsonObject();
            for (Field field : fieldList) {
                jsonObject.addProperty(field.getFieldName(), field.getValue());
            }
            requestParams = gson.toJson(jsonObject);
        }
        Map<String, Object> params = gson.fromJson(requestParams,
                new TypeToken<Map<String, Object>>() {}.getType());

        // 调用方法
        LantuApiClient lantuApiClient = new LantuApiClient(accessKey, secretKey);
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setMethod(interfaceInfo.getMethod());
        currencyRequest.setPath(interfaceInfo.getUrl());
        currencyRequest.setRequestParams(params);
        try {
            com.shuaigef.lantuclientsdk.model.response.BaseResponse response = apiService
                    .request(lantuApiClient, currencyRequest);
            return ResultUtils.success(response.getData());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }


}
