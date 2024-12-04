package com.shuaigef.lantuapibackend.exception;

import com.shuaigef.lantuapicommon.common.BaseResponse;
import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException：----------------{}", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException：----------------{}", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    // region 权限校验相关

    @ExceptionHandler(value = BadCredentialsException.class)
    public BaseResponse handler(BadCredentialsException e) {
        log.error("用户名或密码错误：----------------{}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public BaseResponse handler(UsernameNotFoundException e) {
        log.error("登录信息有误：----------------{}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = JwtCheckException.class)
    public BaseResponse handler(JwtCheckException e) {
        log.error("jwt令牌校验异常：----------------{}", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public BaseResponse handler(AccessDeniedException e) {
        log.error("权限不足：----------------{}", e.getMessage());
        return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR);
    }

    // endregion

    // region 实体类属性校验相关

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse handler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, objectError.getDefaultMessage());
    }


    @ExceptionHandler(value = BindException.class)
    public BaseResponse handler(BindException e) {
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, objectError.getDefaultMessage());
    }

    // endregion

}
