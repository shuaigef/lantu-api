import http from "../plugins/request.ts";

export async function listInterfaceInfoByPage(data: API.ListInterfaceInfoParams) {
    return http.request<API.Page<API.InterfaceInfo>>({
        url: "/interface/list",
        method: "GET",
        params: data
    })
}

export async function getInterfaceInfoById(id: string) {
    return http.request<API.InterfaceInfo>({
        url: "/interface/id",
        method: "GET",
        params: {
            id
        }
    })
}

export async function addInterfaceInfo(data: API.InterfaceInfoAddParams) {
    return http.request({
        url: "/interface",
        method: "POST",
        data: data
    })
}

export async function updateInterfaceInfo(data: API.InterfaceInfoUpdateParams) {
    return http.request({
        url: "/interface",
        method: "PUT",
        data: data
    })
}

export async function deleteInterfaceInfo(id: string) {
    return http.request({
        url: "/interface",
        method: "DELETE",
        data: {
            id
        }
    })
}

export async function deleteBatchInterfaceInfo(ids: string[]) {
    return http.request({
        url: "/interface/ids",
        method: "DELETE",
        data: {
            ids
        }
    })
}

export async function onlineInterfaceInfo(id: string) {
    return http.request({
        url: "/interface/online",
        method: "PUT",
        data: {
            id
        }
    })
}

export async function offlineInterfaceInfo(id: string) {
    return http.request({
        url: "/interface/offline",
        method: "PUT",
        data: {
            id
        }
    })
}

export async function invokeInterfaceInfo(data: API.InterfaceInfoInvokeParams) {
    return http.request({
        url: "/interface/invoke",
        method: "POST",
        data: data
    })
}