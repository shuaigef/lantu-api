<template>
  <div class="register">
    <div class="main">
      <div class="top">
        <div class="title">注册蓝图API开放平台</div>
      </div>
      <a-form class="register-form" :model="formData" layout="vertical" @submit="onSubmit">
        <a-form-item field="username" label="用户名">
          <a-input v-model="formData.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item field="email" label="邮箱">
          <a-input v-model="formData.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item field="password" label="密码">
          <a-input-password
              v-model="formData.password"
              placeholder="请输入密码"
              :defaultVisibility="true"
              allow-clear
          />
        </a-form-item>
        <a-form-item field="checkPassword" label="确认密码">
          <a-input-password
              v-model="formData.checkPassword"
              placeholder="请输入确认密码"
              :defaultVisibility="true"
              allow-clear
          />
        </a-form-item>
        <a-form-item field="verificationCode" label="验证码">
          <a-input v-model="formData.verificationCode" placeholder="请输入验证码" />
          <a-button style="margin-left: 10px" type="primary" :disabled="isActive" @click="handleSend">{{ isActive? `请${time} 秒后重新获取` : '点击发送验证码' }}</a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" long size="large">注册</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {listRole} from "../api/role";
import {Message} from "@arco-design/web-vue";
import {getInterfaceInfoById} from "../api/interfaceInfo";
import {register, sendVerificationCode} from "../api/system";
import {useRouter} from "vue-router";
import { useIntervalFn } from '@vueuse/core'
import {VerificationCodeBizEnum} from "../constants/index";

// region 倒计时
const time = ref(0)
const { pause, resume, isActive } = useIntervalFn(() => {
  /* your function */
  time.value--
  if (time.value <= 0) {
    pause()
  }
}, 1000)
onMounted(() => {
  pause()
})
const handleSend = async () => {
  try {
    const res = await sendVerificationCode(formData.value.email, VerificationCodeBizEnum.EMAIL_REGISTER)
    if (res.code === 0) {
      time.value = 60
      resume()
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
// endregion

const router = useRouter()

const formData = ref<API.UserRegisterParams>({
  username: "",
  email: "",
  password: "",
  checkPassword: "",
  verificationCode: ""
})

const onSubmit = async () => {
  try {
    const res = await register(formData.value)
    if (res.code === 0) {
      Message.success(res.message)
      await router.push({
        name: "login"
      })
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}

</script>

<style scoped lang="scss">
.register {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .main {
    width: 368px;
    .top {
      .title {
        color: #1d2129;
        font-weight: 500;
        font-size: 24px;
        line-height: 32px;
      }
    }
    .register-form {
      margin-top: 32px
    }
  }
}

</style>