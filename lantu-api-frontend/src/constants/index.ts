/** logo 地址 */
export const LOGO_URL = "/public/logo-hand-circle.png"

/** 本地缓存枚举 */
export const enum LocalStorageEnum {
    LOGIN_USER = "login_user",
    JWT = "jwt",
}

/** 本地缓存枚举 */
export const enum UserRoleEnum {
    USER = "user",
    ADMIN = "admin",
    BAN = "ban"
}

/** COS 访问地址 */
export const COS_HOST = "https://github.com/shuaigef"

/** 文件上传业务类型枚举 */
export const enum FileUploadBizEnum {
    /** 用户头像 */
    USER_AVATAR = "user_avatar",
    /** 接口头像 */
    INTERFACE_AVATAR = "interface_avatar"
}

/** 验证码发送业务类型枚举 */
export const enum VerificationCodeBizEnum {
    /** 邮箱注册 */
    EMAIL_REGISTER = "email_register",
    /** 邮箱修改 */
    EMAIL_UPDATE = "email_update"
}