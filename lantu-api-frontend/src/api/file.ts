import http from "../plugins/request";
import {AxiosProgressEvent} from "axios";

/** 文件上传接口 POST /file/upload */
export async function uploadFile(biz: string, file: any, onUploadProgress?: (progressEvent: AxiosProgressEvent) => void) {

    const formData = new FormData()
    formData.append("file", file)
    formData.append("biz", biz)

    return http.request<string>({
        url: "/file/upload",
        method: "POST",
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data; charset=UTF-8",
        },
        onUploadProgress
    })
}