package com.shuaigef.lantuapibackend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuaigef.lantuapibackend.common.utils.SecurityUtils;
import com.shuaigef.lantuapibackend.constant.RedisConstant;
import com.shuaigef.lantuapibackend.constant.SecurityConstant;
import com.shuaigef.lantuapibackend.exception.BusinessException;
import com.shuaigef.lantuapibackend.mapper.UserMapper;
import com.shuaigef.lantuapibackend.model.dto.user.UserAddRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserEmailUpdateRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserQueryRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserRegisterRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePasswordRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdatePersonalDetailRequest;
import com.shuaigef.lantuapibackend.model.dto.user.UserUpdateRequest;
import com.shuaigef.lantuapibackend.model.entity.Role;
import com.shuaigef.lantuapibackend.model.enums.UserGenderEnum;
import com.shuaigef.lantuapibackend.model.enums.VerificationCodeBizEnum;
import com.shuaigef.lantuapibackend.model.vo.UserVO;
import com.shuaigef.lantuapibackend.service.RoleService;
import com.shuaigef.lantuapibackend.service.UserService;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 用户服务实现
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private RoleService roleService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 盐值
     */
    private final static String SALT = "shuaigef";

    /**
     * 用户初始积分
     */
    private final static Integer DEFAULT_USER_POINTS = 10;

    /**
     * 默认用户名前缀
     */
    private final static String DEFAULT_USERNAME_PREFIX = "用户";

    @Override
    public boolean register(UserRegisterRequest userRegisterRequest) {
        String username = userRegisterRequest.getUsername();
        String email = userRegisterRequest.getEmail();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String verificationCode = userRegisterRequest.getVerificationCode();

        // 密码输入不一致
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterRequest, user);
        this.validateUser(user, false);

        // 验证码校验
        String redisKey = VerificationCodeBizEnum.EMAIL_REGISTER.getValue() + RedisConstant.VERIFICATION_CODE_KEY + email;
        String currentVerificationCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (!verificationCode.equals(currentVerificationCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 删除已使用验证码
        stringRedisTemplate.delete(redisKey);

        // 生成签名 ak sk
        String accessKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(5));
        String secretKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(8));

        String nickname = DEFAULT_USERNAME_PREFIX + RandomUtil.randomNumbers(4);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        // todo 普通用户改为常量或字典
        user.setRoleId(2l);
        user.setGender(UserGenderEnum.UNKNOWN.getValue());
        user.setUserPoints(DEFAULT_USER_POINTS);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        return this.save(user);
    }

    @Override
    public long addUser(UserAddRequest userAddRequest) {
        String username = userAddRequest.getUsername();
        String password = userAddRequest.getPassword();
        String checkPassword = userAddRequest.getCheckPassword();

        // 两次密码不一致
        if (!StringUtils.equals(password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        this.validateUser(user, false);

        // 生成签名 ak sk
        String accessKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(5));
        String secretKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(8));

        user.setPassword(passwordEncoder.encode(password));
        user.setUserPoints(DEFAULT_USER_POINTS);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增失败");
        }
        return user.getId();
    }

    @Override
    public boolean deleteUser(long id) {
        // 删除用户不能为管理员
        if (SecurityConstant.ADMIN_USER_ID.compareTo(id) == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能删除管理员");
        }
        // 用户不存在
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public boolean deleteBatchUser(List<Long> ids) {
        // 删除用户不能为管理员
        if (CollectionUtil.contains(ids, SecurityConstant.ADMIN_USER_ID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能删除管理员");
        }
        boolean result = this
                .remove(new LambdaQueryWrapper<User>().in(User::getId, ids));
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量删除失败");
        }
        return true;
    }

    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        this.validateUser(user, true);

        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新用户失败");
        }
        return result;
    }

    @Override
    public boolean updatePersonalDetail(UserUpdatePersonalDetailRequest userUpdatePersonalDetailRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdatePersonalDetailRequest, user);
        user.setId(SecurityUtils.getCurrentUserId());
        validateUser(user, true);

        return this.updateById(user);
    }

    @Override
    public boolean updatePersonalPassword(UserUpdatePasswordRequest userUpdatePasswordRequest) {
        String oldPassword = userUpdatePasswordRequest.getOldPassword();
        String newPassword = userUpdatePasswordRequest.getNewPassword();
        String checkPassword = userUpdatePasswordRequest.getCheckPassword();

        long currentUserId = SecurityUtils.getCurrentUserId();

        // 两次密码输入不一致
        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        // 原始密码错误
        User currentUser = this.getById(currentUserId);
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原始密码错误");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        return this.updateById(currentUser);
    }

    @Override
    public Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest) {
        String username = userQueryRequest.getUsername();
        String nickname = userQueryRequest.getNickname();
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), User::getUsername, username)
                .like(StringUtils.isNotBlank(nickname), User::getNickname, nickname);
        Page<User> userPage = this.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = this.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    @Override
    public User getCurrentUser() {
        long currentUserId = SecurityUtils.getCurrentUserId();
        User user = this.getById(currentUserId);
        return user;
    }

    @Override
    public boolean reduceUserPoints(long userId, int reducePoints) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getUserPoints, user.getUserPoints() - reducePoints);
        return this.update(updateWrapper);
    }

    @Override
    public boolean updateUserEmail(UserEmailUpdateRequest userEmailUpdateRequest) {
        String email = userEmailUpdateRequest.getEmail();
        String verificationCode = userEmailUpdateRequest.getVerificationCode();

        // 验证码校验
        String redisKey = VerificationCodeBizEnum.EMAIL_UPDATE.getValue() + RedisConstant.VERIFICATION_CODE_KEY + email;
        String currentVerificationCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (!verificationCode.equals(currentVerificationCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 删除已使用验证码
        stringRedisTemplate.delete(redisKey);

        long currentUserId = SecurityUtils.getCurrentUserId();

        // 邮箱已使用
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, currentUserId));
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已使用");
        }

        user = new User();
        user.setId(currentUserId);
        user.setEmail(email);
        return this.updateById(user);
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 用户角色类型转换 id -> roleName
        userVO.setUserRole(roleService.getById(user.getRoleId()).getRoleName());
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 用户字段校验
     *
     * @param user 用户信息
     * @param isUpdate 是否为更新用户，如果更新则排除 id == user.getId() 的记录
     */
    private void validateUser(User user, boolean isUpdate) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 用户不存在
        Long id = user.getId();
        if (isUpdate && this.getById(id) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }

        // 校验用户名
        String username = user.getUsername();
        if (StringUtils.isNotBlank(username)) {
            if (isUpdate && id != null && SecurityConstant.ADMIN_USER_ID.equals(id)) {
                if (!SecurityConstant.ADMIN_USERNAME.equals(username)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能修改管理员用户名");
                }
            } else {
                // 非管理员用户名不能为 admin
                if (SecurityConstant.ADMIN_USERNAME.equals(username)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名异常");
                }
                this.validateUniqueField(User::getUsername, username, id, isUpdate, "用户名已存在");
            }
        }

        // 校验手机号
        String phoneNumber = user.getPhoneNumber();
        if (StringUtils.isNotBlank(phoneNumber)) {
            this.validateUniqueField(User::getPhoneNumber, phoneNumber, id, isUpdate, "手机号已注册");
        }

        // 校验邮箱
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email)) {
            this.validateUniqueField(User::getEmail, email, id, isUpdate, "邮箱已注册");
        }

        // 校验角色
        Long roleId = user.getRoleId();
        if (roleId != null && roleId > 0) {
            // 角色是否为管理员以及是否存在
            if (SecurityConstant.ADMIN_ROLE_ID.compareTo(roleId) == 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色不能为管理员");
            }
            Role role = roleService.getById(roleId);
            if (role == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色不存在");
            }
        }

        // 校验性别
        Integer gender = user.getGender();
        if (gender != null && UserGenderEnum.getEnumByValue(gender) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别错误");
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
    private void validateUniqueField(SFunction<User, ?> column, Object value, Long id, boolean isUpdate, String errorMsg) {
        User existingUser = this.getOne(new LambdaQueryWrapper<User>()
                .eq(column, value)
                .ne(isUpdate && id != null, User::getId, id));
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, errorMsg);
        }
    }

}




