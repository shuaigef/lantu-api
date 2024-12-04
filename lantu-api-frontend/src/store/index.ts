import {defineStore} from "pinia";
import {onMounted, ref, watch} from "vue";
import {LocalStorageEnum} from "../constants/index";

export const useIndexStore = defineStore("index", () => {
    const str = "hello world"

    return {
        str
    }
})

export const useUserStore = defineStore("user", () => {
    const str = "hello world"

    return {
        str
    }
})

export const useSystemStore = defineStore("system", () => {
    const loginUserStr = ref(localStorage.getItem(LocalStorageEnum.LOGIN_USER))
    const loginUser = ref<API.LoginUser | null>(loginUserStr.value ? JSON.parse(loginUserStr.value) : null)

    // watch(
    //     () => loginUserStr.value,
    //     (newValue) => {
    //         if (newValue) {
    //             loginUser.value = JSON.parse(newValue)
    //         } else {
    //             loginUser.value = null
    //         }
    //     }
    // )

    return {
        loginUser
    }
})