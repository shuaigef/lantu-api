package com.shuaigef.lantuapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuaigef.lantuapibackend.common.request.DeleteBatchRequest;
import com.shuaigef.lantuapibackend.common.request.DeleteRequest;
import com.shuaigef.lantuapibackend.common.request.IdRequest;
import com.shuaigef.lantuapibackend.common.utils.JwtUtils;
import com.shuaigef.lantuapibackend.common.utils.SecurityUtils;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.model.dto.user.UserAddRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePasswordRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePersonalDetailRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdateRequest;
import com.shuaigef.lantuapibackend.model.entity.SessionUser;
import com.shuaigef.lantuapibackend.model.vo.LoginUserVO;
import com.shuaigef.lantuapibackend.model.vo.UserVO;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.common.BaseResponse;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.common.ResultUtils;
import com.shuaigef.lantuapicommon.model.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Api(tags = "系统管理-用户管理")
@RestController
@RequestMapping("/manage/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@roleCheckService.hasPermission('systemManage:userManage')")
    public BaseResponse<Long> addUser(@Valid @RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(userService.addUser(userAddRequest), "新增用户成功");
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@roleCheckService.hasPermission('systemManage:userManage')")
    public BaseResponse<Boolean> deleteUser(@Valid @RequestBody DeleteRequest deleteRequest) {
        boolean result = userService.deleteUser(deleteRequest.getId());
        return ResultUtils.success(result, "删除用户成功");
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("/ids")
    @PreAuthorize("@roleCheckService.hasPermission('systemManage:userManage')")
    public BaseResponse<Boolean> deleteBatchUser(@Valid @RequestBody DeleteBatchRequest deleteBatchRequest) {
        boolean result = userService.deleteBatchUser(deleteBatchRequest.getIds());
        return ResultUtils.success(result, "批量删除用户成功");
    }

    @ApiOperation("根据 id 查询用户")
    @GetMapping("/id")
    public BaseResponse<UserVO> getUserById(@Valid @RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id 必须大于 0");
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该角色不存在");
        }
        return ResultUtils.success(userService.getUserVO(user), "查询用户成功");
    }

    @ApiOperation("分页查询用户列表")
    @GetMapping("/list/page")
    @PreAuthorize("@roleCheckService.hasPermission('systemManage:userManage')")
    public BaseResponse<Page<UserVO>> listUserByPage(@Valid UserQueryRequest userQueryRequest) {
        return ResultUtils.success(userService.listUserByPage(userQueryRequest), "查询用户列表成功");
    }

    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@roleCheckService.hasPermission('systemManage:userManage')")
    public BaseResponse<Boolean> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResultUtils.success(userService.updateUser(userUpdateRequest), "更新用户成功");
    }

    @ApiOperation("修改用户个人信息")
    @PutMapping("/personal/detail")
    public BaseResponse<LoginUserVO> updatePersonalDetail(@Valid @RequestBody
            UserUpdatePersonalDetailRequest userUpdatePersonalDetailRequest) {
        String username = userUpdatePersonalDetailRequest.getUsername();
        String nickname = userUpdatePersonalDetailRequest.getNickname();
        String userAvatar = userUpdatePersonalDetailRequest.getUserAvatar();
        String userProfile = userUpdatePersonalDetailRequest.getUserProfile();
        Integer gender = userUpdatePersonalDetailRequest.getGender();

        // 更新数据库
        userService.updatePersonalDetail(userUpdatePersonalDetailRequest);
        // 生成新的jwt
        SessionUser currentUser = SecurityUtils.getCurrentUser();
        BeanUtils.copyProperties(userUpdatePersonalDetailRequest, currentUser);
        SessionUser sessionUser = new SessionUser(
                username,
                "",
                new ArrayList<>(),
                currentUser.getUserId(),
                currentUser.getRoleId(),
                nickname, userAvatar, userProfile, gender);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(sessionUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.createToken(authentication);

        return ResultUtils.success(new LoginUserVO(token, sessionUser, null), "修改个人信息成功");
    }

    @ApiOperation("修改用户密码")
    @PutMapping("/personal/password")
    public BaseResponse<Boolean> userUpdatePassword(@Valid @RequestBody
            UserUpdatePasswordRequest userUpdatePasswordRequest) {
        return ResultUtils.success(userService.updatePersonalPassword(userUpdatePasswordRequest), "修改密码成功");
    }


}
