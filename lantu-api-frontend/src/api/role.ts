import http from "../plugins/request";

export async function listRole() {
    return http.request<API.Role[]>({
        url: "/manage/role/list",
        method: "GET"
    })
}

export async function queryRoleByPage(data: API.RoleQueryParams) {
    return http.request<API.Page<API.Role>>({
        url: "/manage/role/list/page",
        method: "GET",
        params: data
    })
}

export async function deleteRole(id: string) {
    return http.request<boolean>({
        url: "/manage/role",
        method: "DELETE",
        data: {
            id
        }
    })
}

export async function deleteBatchRole(ids: string[]) {
    return http.request<boolean>({
        url: "/manage/role/ids",
        method: "DELETE",
        data: {
            ids
        }
    })
}

export async function addRole(data: API.RoleAddParams) {
    return http.request<string>({
        url: "/manage/role",
        method: "POST",
        data: data
    })
}

export async function updateRole(data: API.RoleUpdateParams) {
    return http.request<boolean>({
        url: "/manage/role",
        method: "PUT",
        data: data
    })
}
