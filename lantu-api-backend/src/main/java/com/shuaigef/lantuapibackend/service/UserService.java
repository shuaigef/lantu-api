package com.shuaigef.lantuapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuaigef.lantuapibackend.model.dto.user.UserAddRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserRegisterRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePasswordRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePersonalDetailRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdateRequest;
import com.shuaigef.lantuapibackend.model.vo.UserVO;
import com.shuaigef.lantuapicommon.model.entity.User;
import java.util.List;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public interface UserService extends IService<User> {

    /**
     * 注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 是否成功
     */
    boolean register(UserRegisterRequest userRegisterRequest);

    /**
     * 添加用户
     *
     * @param userAddRequest 用户新增请求
     * @return 新增用户 id
     */
    long addUser(UserAddRequest userAddRequest);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否成功
     */
    boolean deleteUser(long id);

    /**
     * 批量删除用户
     *
     * @param ids 用户id
     * @return 是否成功
     */
    boolean deleteBatchUser(List<Long> ids);

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新请求
     * @return 是否成功
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 用户更新个人信息
     *
     * @param userUpdatePersonalDetailRequest 用户更新个人信息请求
     * @return 是否成功
     */
    boolean updatePersonalDetail(UserUpdatePersonalDetailRequest userUpdatePersonalDetailRequest);

    /**
     * 用户更新密码
     *
     * @param userUpdatePasswordRequest 用户更新密码请求
     * @return 是否成功
     */
    boolean updatePersonalPassword(UserUpdatePasswordRequest userUpdatePasswordRequest);

    /**
     * 分页查询用户
     *
     * @param userQueryRequest 分页查询用户请求
     * @return
     */
    Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    User getCurrentUser();

    /**
     * 扣除用户积分
     *
     * @param userId 用户id
     * @param reducePoints 扣除的积分
     * @return 是否成功
     */
    boolean reduceUserPoints(long userId, int reducePoints);

    /**
     * 用户信息脱敏
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 用户信息脱敏
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);
}
