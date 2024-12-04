import http from "../plugins/request";

export async function login(body: API.UserLoginParams) {
    return http.request<API.LoginUser>({
        url: "/system/login",
        method: "POST",
        data: body
    })
}

export async function register(body: API.UserRegisterParams) {
    return http.request({
        url: "/system/register",
        method: "POST",
        data: body
    })
}

export async function sendEmailVerificationCode(email: string) {
    return http.request({
        url: "/system/send",
        method: "POST",
        data: {
            email
        }
    })
}

export async function logout() {
    return http.request<boolean>({
        url: "/system/logout",
        method: "POST"
    })
}


