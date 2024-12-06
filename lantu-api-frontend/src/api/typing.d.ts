declare namespace API {
    // region 通用
    /** 自定义请求返回 data 格式 */
    type BaseResponse<T = any> = {
        code: number;
        data: T; // 这里定义请求返回data数据类型
        message: string;
    }

    /** 分页请求参数 */
    type PageRequest = {
        current: number,
        pageSize: number,
        sortField?: string,
        sortOrder?: string,
    }

    type Page<T> = {
        records: T[],
        total: number,
        size: number,
        current: number,
    }
    // endregion

    // region user
    type UserLoginParams = {
        usernameOrEmail?: string,
        password?: string
    }

    type UserRegisterParams = {
        username?: string,
        email?: string,
        password?: string,
        checkPassword?: string,
        verificationCode?: string
    }

    type SessionUser = {
        userId?: string,
        roleId?: string,
        nickname?: string,
        userAvatar?: string,
        gender?: number,
        username?: string
    }

    type AuthorityMeta = {
        hideMenu?: boolean,
        icon?: string,
        componentName?: string,
        title?: string
    }

    type Authority = {
        id: string,
        code?: string,
        name?: string,
        orderNo?: number,
        parentId?: string,
        authorityType?: string,
        routePath?: string,
        hidden?: number,
        menuIcon?: string,
        component?: string,
        componentName?: string,
        createTime?: string,
        updateTime?: string,
        meta?: AuthorityMeta,
        children?: Authority[],
        check?: boolean,
        node?: boolean
    }

    type AuthorityQueryParams = {
        code?: string,
        name?: string,
        authorityType?: string,
    } & PageRequest

    type AuthorityAddParams = {
        code?: string,
        name?: string,
        orderNo?: number,
        parentId?: string,
        authorityType?: string,
        routePath?: string,
        hidden?: number,
        menuIcon?: string,
        component?: string,
        componentName?: string,
    }

    type AuthorityUpdateParams = {
        id?: string,
        code?: string,
        name?: string,
        orderNo?: number,
        parentId?: string,
        authorityType?: string,
        routePath?: string,
        hidden?: number,
        menuIcon?: string,
        component?: string,
        componentName?: string,
    }

    type RoleAuthorityUpdateParams = {
        roleId?: string,
        authorityIds?: string[]
    }

    type Role = {
        id: string,
        roleName?: string,
        roleDesc?: string,
        createTime?: string
    }

    type RoleAddParams = {
        roleName?: string,
        roleDesc?: string,
    }

    type RoleQueryParams = {
        roleName?: string,
    } & PageRequest

    type RoleUpdateParams = {
        id?: string,
        roleName?: string,
        roleDesc?: string,
    }

    type User = {
        id: string,
        username?: string,
        nickname?: string,
        userAvatar?: string,
        userProfile?: string,
        gender?: number,
        email?: string,
        phoneNumber?: string,
        roleId?: string,
        userRole?: string,
        userPoints?: number,
        accessKey?: string,
        secretKey?: string,
        createTime?: string
    }

    type LoginUser = {
        token?: string,
        userInfo?: SessionUser,
        authorityList?: Authority[]
    }

    type UserQueryParams = {
        username?: string,
        nickname?: string
    } & PageRequest

    type UserAddParams = {
        username?: string,
        password?: string,
        checkPassword?: string,
        nickname?: string,
        userAvatar?: string,
        userProfile?: string,
        roleId?: string,
        gender?: number,
        email?: string,
        phoneNumber?: string
    }

    type UserUpdateParams = {
        id?: string,
        username?: string,
        nickname?: string,
        userAvatar?: string,
        userProfile?: string,
        roleId?: string,
        gender?: number,
        email?: string,
        phoneNumber?: string
    }

    type UserUpdatePersonalDetailParams = {
        username?: string,
        nickname?: string,
        userAvatar?: string,
        userProfile?: string,
        gender?: number,
    }

    type UserUpdatePasswordParams = {
        oldPassword?: string,
        newPassword?: string,
        checkPassword?: string
    }

    type UserEmailUpdateParams = {
        email: string,
        verificationCode: string
    }
    // endregion

    // region interface
    type InterfaceInfo = {
        id?: string,
        name?: string,
        description?: string,
        interfaceAvatar?: string,
        url?: string,
        requestParams?: RequestParamsField[],
        requestHeader?: Field[],
        responseHeader?: Field[],
        status?: number,
        method?: string,
        userId?: string,
        totalNum?: number,
        consumePoints?: number,
        createTime?: string,
        updateTime?: string
    }

    type ListInterfaceInfoParams = {
        name?: string,
        status?: number
    } & PageRequest

    type RequestParamsField = {
        fieldName?: string,
        type?: string,
        description?: string
    }

    type InterfaceInfoAddParams = {
        name?: string,
        description?: string,
        interfaceAvatar?: string,
        consumePoints?: number,
        url?: string,
        requestParams?: RequestParamsField[],
        requestHeader?: Field[],
        responseHeader?: Field[],
        method?: string,
    }

    type InterfaceInfoUpdateParams = {
        id?: string,
        name?: string,
        description?: string,
        interfaceAvatar?: string,
        consumePoints?: number,
        url?: string,
        requestParams?: RequestParamsField[],
        requestHeader?: Field[],
        responseHeader?: Field[],
        method?: string,
    }

    type Field = {
        fieldName?: string,
        value?: string
    }

    type InterfaceInfoInvokeParams = {
        id?: string,
        requestParams?: Field[]
    }
    // endregion

}
