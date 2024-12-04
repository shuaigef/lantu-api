import http from "../plugins/request";

export async function getAuthorityTreeByRoleId(roleId: string) {
    return http.request<API.Authority[]>({
        url: "/manage/authority/tree",
        method: "GET",
        params: {
            roleId
        }
    })
}

export async function roleBindAuthority(data: API.RoleAuthorityUpdateParams) {
    return http.request<void>({
        url: "/manage/authority/roleBindAuthority",
        method: "PUT",
        data: data
    })
}

export async function queryAuthorityByPage(data: API.AuthorityQueryParams) {
    return http.request<API.Page<API.Authority>>({
        url: "/manage/authority/list/page",
        method: "GET",
        params: data
    })
}

export async function addAuthority(data: API.AuthorityAddParams) {
    return http.request<string>({
        url: "/manage/authority",
        method: "POST",
        data: data
    })
}

export async function deleteAuthority(id: string) {
    return http.request<boolean>({
        url: "/manage/authority",
        method: "DELETE",
        data: {
            id
        }
    })
}

export async function deleteBatchAuthority(ids: string[]) {
    return http.request<boolean>({
        url: "/manage/authority/ids",
        method: "DELETE",
        data: {
            ids
        }
    })
}

export async function updateAuthority(data: API.AuthorityUpdateParams) {
    return http.request<boolean>({
        url: "/manage/authority",
        method: "PUT",
        data: data
    })
}

export async function resetAdminAuthority() {
    return http.request<void>({
        url: "/manage/authority/reset/admin",
        method: "PUT"
    })
}