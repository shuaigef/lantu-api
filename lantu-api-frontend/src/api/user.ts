/** 管理员登录接口 POST /user/login/admin */
import http from "../plugins/request";

export async function queryUserByPage(data: API.UserQueryParams) {
    return http.request<API.Page<API.User>>({
        url: "/manage/user/list/page",
        method: "GET",
        params: data
    })
}

export async function addUser(data: API.UserAddParams) {
    return http.request<boolean>({
        url: "/manage/user",
        method: "POST",
        data: data
    })
}

export async function updateUser(data: API.UserUpdateParams) {
    return http.request<boolean>({
        url: "/manage/user",
        method: "PUT",
        data: data
    })
}

export async function deleteUser(id: string) {
    return http.request<boolean>({
        url: "/manage/user",
        method: "DELETE",
        data: {
            id
        }
    })
}

export async function deleteBatchUser(ids: string[]) {
    return http.request<boolean>({
        url: "/manage/user/ids",
        method: "DELETE",
        data: {
            ids
        }
    })
}

export async function updatePersonalDetail(data: API.UserUpdateParams) {
    return http.request<API.LoginUser>({
        url: "/manage/user/personal/detail",
        method: "PUT",
        data: data
    })
}

export async function updatePersonalPassword(data: API.UserUpdatePasswordParams) {
    return http.request<API.LoginUser>({
        url: "/manage/user/personal/password",
        method: "PUT",
        data: data
    })
}

