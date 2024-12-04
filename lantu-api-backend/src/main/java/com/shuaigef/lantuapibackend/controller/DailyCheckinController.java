package com.shuaigef.lantuapibackend.controller;

import com.shuaigef.lantuapibackend.common.utils.SecurityUtils;
import com.shuaigef.lantuapibackend.service.DailyCheckinService;
import com.shuaigef.lantuapicommon.common.BaseResponse;
import com.shuaigef.lantuapicommon.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每日签到接口
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 **/
@Api(tags = "每日签到")
@RestController
@RequestMapping("/daily/checkin")
public class DailyCheckinController {

    @Resource
    private DailyCheckinService dailyCheckinService;

    @ApiOperation("每日签到")
    @PostMapping
    public BaseResponse<Boolean> handleDailyCheckin() {
        return ResultUtils.success(dailyCheckinService.handleDailyCheckin(), "签到成功");
    }

    @ApiOperation("今日是否签到")
    @GetMapping
    public BaseResponse<Boolean> isCheckin() {
        long currentUserId = SecurityUtils.getCurrentUserId();
        LocalDate localDate = LocalDate.now();
        boolean checkinResult = dailyCheckinService.isCheckin(currentUserId, localDate);
        if (checkinResult) {
            return ResultUtils.success(true, "今日已签到");
        } else {
            return ResultUtils.success(false, "今日未签到");
        }
    }

}
