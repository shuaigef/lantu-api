package com.shuaigef.lantuapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuaigef.lantuapibackend.model.entity.DailyCheckin;
import java.time.LocalDate;

/**
 * 每日签到服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface DailyCheckinService extends IService<DailyCheckin> {

    /**
     * 每日签到
     *
     * @return 是否成功
     */
    boolean handleDailyCheckin();

    /**
     * 今日是否签到
     *
     * @param userId 用户id
     * @param localDate 日期
     * @return 是否签到
     */
    boolean isCheckin(long userId, LocalDate localDate);

}
