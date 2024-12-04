package com.shuaigef.lantuapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuaigef.lantuapibackend.common.utils.SecurityUtils;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.mapper.DailyCheckinMapper;
import com.shuaigef.lantuapibackend.model.entity.DailyCheckin;
import com.shuaigef.lantuapibackend.service.DailyCheckinService;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.dubbo.common.logger.FluentLogger.S;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import sun.jvm.hotspot.interpreter.BytecodeSipush;

/**
 * 每日签到服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Service
public class DailyCheckinServiceImpl extends ServiceImpl<DailyCheckinMapper, DailyCheckin>
    implements DailyCheckinService {

    @Resource
    private UserService userService;

    /**
     * 签到获得的积分
     */
    private int CHECKIN_POINTS = 20;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean handleDailyCheckin() {
        User currentUser = userService.getCurrentUser();
        LocalDate localDate = LocalDate.now();
        // 今日是否已签到
        if (this.isCheckin(currentUser.getId(), localDate)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "今日已签到");
        }

        // 新增签到记录
        DailyCheckin dailyCheckin = new DailyCheckin();
        dailyCheckin.setUserid(currentUser.getId());
        dailyCheckin.setPoints(CHECKIN_POINTS);
        dailyCheckin.setCheckinTime(localDate);
        this.save(dailyCheckin);
        // 增加用户积分
        currentUser.setUserPoints(currentUser.getUserPoints() + CHECKIN_POINTS);
        userService.updateById(currentUser);

        return true;
    }

    @Override
    public boolean isCheckin(long userId, LocalDate localDate) {
        DailyCheckin dailyCheckin = this.getOne(new LambdaQueryWrapper<DailyCheckin>()
                .eq(DailyCheckin::getUserid, userId)
                .eq(DailyCheckin::getCheckinTime, localDate));
        return dailyCheckin != null;
    }
}




