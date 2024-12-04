package com.shuaigef.lantuapibackend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.mapper.UserMapper;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.User;
import com.shuaigef.lantuapicommon.service.InnerUserService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 远程调用用户服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "accessKey 不能为空");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey, accessKey);

        return userMapper.selectOne(queryWrapper);
    }
}
