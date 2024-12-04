package com.shuaigef.lantuapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.mapper.UserInterfaceInfoMapper;
import com.shuaigef.lantuapibackend.model.enums.UserInterfaceInfoStatusEnum;
import com.shuaigef.lantuapibackend.service.UserInterfaceInfoService;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.UserInterfaceInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户接口服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserService userService;

    @Override
    public int getTotalNumById(long interfaceId) {
        return this.list(new LambdaQueryWrapper<UserInterfaceInfo>()
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceId))
                .stream()
                .mapToInt(UserInterfaceInfo::getTotalNum)
                .sum();
    }

    @Override
    public boolean invoke(long interfaceInfoId, long userId, int reducePoints) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果该记录不存在，则新建
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);
        boolean updateInvokeResult;
        if (userInterfaceInfo == null) {
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setTotalNum(1);
            userInterfaceInfo.setStatus(UserInterfaceInfoStatusEnum.NORMAL.getValue());
            updateInvokeResult = this.save(userInterfaceInfo);
        } else {
            // 更新该用户调用该接口的次数
            LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
            updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
            updateWrapper.set(UserInterfaceInfo::getTotalNum, userInterfaceInfo.getTotalNum() + 1);
            updateInvokeResult = this.update(updateWrapper);
        }
        // 扣除积分
        boolean updatePointsResult = userService.reduceUserPoints(userId, reducePoints);
        if (!(updateInvokeResult && updatePointsResult)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用失败");
        }
        return true;
    }

}




