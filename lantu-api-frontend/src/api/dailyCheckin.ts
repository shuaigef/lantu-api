import http from "../plugins/request";

export async function handleDailyCheckin() {
    return http.request<boolean>({
        url: "/daily/checkin",
        method: "POST"
    })
}

export async function isCheckin() {
    return http.request<boolean>({
        url: "/daily/checkin",
        method: "GET"
    })
}