import http from "../plugins/request";

export async function listTopInvokeInterfaceInfo() {
    return http.request<API.InterfaceInfo>({
        url: "/analysis/top/interface/invoke",
        method: "GET",
    })
}