<template>
  <div class="login">
    <div class="main">
      <div class="top">
        <div class="title">登录蓝图API开放平台</div>
      </div>
      <a-form :model="userLoginParams" name="normal_login" class="login-form" @submit="onSubmit">
        <a-form-item label="账号" name="username" hide-label>
          <a-input v-model="userLoginParams.usernameOrEmail" placeholder="账号" size="large">
            <template #prefix>
              <icon-user />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item label="密码" name="password" hide-label>
          <a-input-password v-model="userLoginParams.password" placeholder="密码" size="large">
            <template #prefix>
              <icon-lock />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item hide-label>
          <a-row justify="space-between" style="width: 100%">
            <a-col span="12">
              <a-checkbox>自动登录</a-checkbox>
            </a-col>
            <a-col span="12" style="text-align: right;">
              <a-link>忘记密码</a-link>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item hide-label>
          <a-button type="primary" html-type="submit" long size="large">登录</a-button>
        </a-form-item>

        <a-form-item hide-label>
          <a-button type="text" long size="large" @click="toRegister" style="color: #86909c">注册</a-button>
        </a-form-item>

<!--        <a-form-item hide-label>-->
<!--          <a-row justify="space-between" style="width: 100%">-->
<!--            <a-col :span="12">-->
<!--              <span>其他登录方式</span>-->
<!--            </a-col>-->
<!--            <a-col :span="12" style="text-align: right;">-->
<!--              <a @click="toRegister">注册账户</a>-->
<!--            </a-col>-->
<!--          </a-row>-->
<!--        </a-form-item>-->

      </a-form>
    </div>
  </div>

</template>

<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import {LocalStorageEnum} from "../constants";
import {login} from "../api/system";
import {useSystemStore} from "../store/index";
import {storeToRefs} from "pinia";
import {Message} from "@arco-design/web-vue";
import {IconUser, IconLock} from '@arco-design/web-vue/es/icon';

const router = useRouter();

const systemStore = useSystemStore()
const { loginUser } = storeToRefs(systemStore)

const userLoginParams = ref<API.UserLoginParams>({
  usernameOrEmail: "",
  password: ""
})

const onSubmit = async () => {
  login(userLoginParams.value).then(res => {
    if (res.code === 0) {
      localStorage.setItem(LocalStorageEnum.JWT, "Bearer " + res.data.token)
      localStorage.setItem(LocalStorageEnum.LOGIN_USER, JSON.stringify(res.data))
      loginUser.value = res.data
      Message.success("登录成功")
      router.push('/index')
    } else {
      Message.error(res.message)
    }
  }).catch(e => {
    Message.error(e.response.data.message)
  })
}

const toRegister = () => {
  router.push({
    name: "register"
  })
}
</script>

<style scoped lang="scss">
.login {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .main {
    width: 368px;
    .top{
      .title {
        color: #1d2129;
        font-weight: 500;
        font-size: 24px;
        line-height: 32px;
      }
    }
    .login-form {
      margin-top: 32px
    }
  }
}


</style>
