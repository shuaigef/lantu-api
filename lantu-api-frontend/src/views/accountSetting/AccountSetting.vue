<template>
  <a-card title="个人信息" :bordered="false">
    <template #extra>
      <a-button type="primary" @click="handleUpdatePersonalDetail">保存</a-button>
    </template>
    <div style="display: flex; justify-content: center; align-items: center">
      <a-form :model="personalDetailFormData" :style="{width: '600px'}">
        <a-form-item field="userAvatar" label="头像">
          <FileUpload :biz="FileUploadBizEnum.USER_AVATAR" listType="picture-card" :limit="1" v-model:fileUrl="personalDetailFormData.userAvatar" />
        </a-form-item>
        <a-form-item field="name" label="用户名">
          <a-input v-model="personalDetailFormData.username" placeholder="请输入用户名"/>
        </a-form-item>
        <a-form-item field="nickname" label="昵称">
          <a-input v-model="personalDetailFormData.nickname" placeholder="请输入昵称"/>
        </a-form-item>
        <a-form-item field="userProfile" label="个人简介">
          <a-textarea v-model="personalDetailFormData.userProfile" placeholder="请输入个人简介" allow-clear/>
        </a-form-item>
        <a-form-item field="gender" label="性别">
          <a-select v-model="personalDetailFormData.gender" placeholder="请选择性别">
            <a-option :value="0">未知</a-option>
            <a-option :value="1">男</a-option>
            <a-option :value="2">女</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
  </a-card>

  <a-card title="修改密码" :bordered="false">
    <template #extra>
      <a-button type="primary" @click="handleUpdatePassword">保存</a-button>
    </template>
    <div style="display: flex; justify-content: center; align-items: center">
      <a-form :model="passwordFormData" :style="{width: '600px'}">
        <a-form-item field="oldPassword" label="旧密码">
          <a-input-password v-model="passwordFormData.oldPassword" placeholder="请输入旧密码"/>
        </a-form-item>
        <a-form-item field="newPassword" label="新密码">
          <a-input-password v-model="passwordFormData.newPassword" placeholder="请输入新密码"/>
        </a-form-item>
        <a-form-item field="checkPassword" label="确认密码">
          <a-input-password v-model="passwordFormData.checkPassword" placeholder="请输入确认密码"/>
        </a-form-item>
      </a-form>
    </div>
  </a-card>

  <a-card title="我的积分" :bordered="false">
<!--    <template #extra>-->
<!--      <a-button type="primary" @click="handleUpdatePassword">充值</a-button>-->
<!--    </template>-->
    <div>
      <div>
        <span>我的积分：</span><span style="color: #f10909; font-size: 16px">{{ userDetail.userPoints || 0 }}</span>
      </div>
      <a-button style="margin-top: 15px" type="primary" :disabled="isCheckinFlag" @click="handleCheckin">每日签到</a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import {useSystemStore} from "../../store/index";
import {storeToRefs} from "pinia";
import {ref} from "vue";
import {FileUploadBizEnum} from '../../constants';
import FileUpload from "../../components/FileUpload.vue";
import {getMyUserDetail, updatePersonalDetail, updatePersonalPassword} from "../../api/user";
import {Message} from "@arco-design/web-vue";
import {LocalStorageEnum} from "../../constants/index";
import {useRouter} from "vue-router";
import {logout} from "../../api/system";
import {handleDailyCheckin, isCheckin} from "../../api/dailyCheckin";

const systemStore = useSystemStore()
const { loginUser } = storeToRefs(systemStore)
const userDetail = ref<API.User>()

const personalDetailFormData = ref<API.UserUpdatePersonalDetailParams>({
  username: loginUser.value.userInfo.username,
  nickname: loginUser.value.userInfo.nickname,
  userAvatar: loginUser.value.userInfo.userAvatar,
  userProfile: loginUser.value.userInfo.userProfile,
  gender: loginUser.value.userInfo.gender,
})

const handleUpdatePersonalDetail = async () => {
  try {
    const res = await updatePersonalDetail(personalDetailFormData.value)
    if (res.code == 0) {
      localStorage.setItem(LocalStorageEnum.JWT, "Bearer " + res.data.token)
      let newLoginUser: API.LoginUser = JSON.parse(localStorage.getItem(LocalStorageEnum.LOGIN_USER) as string)
      newLoginUser.userInfo = res.data.userInfo
      newLoginUser.token = res.data.token
      localStorage.setItem(LocalStorageEnum.LOGIN_USER, JSON.stringify(newLoginUser))
      loginUser.value = newLoginUser
      Message.success(res.message)
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}

const passwordFormData = ref<API.UserUpdatePasswordParams>({
  oldPassword: "",
  newPassword: "",
  checkPassword: ""
})
const router = useRouter()
const handleUpdatePassword = async () => {
  try {
    const res = await updatePersonalPassword(passwordFormData.value)
    if (res.code == 0) {
      Message.success(res.message)
      // 调用后端登出接口
      const logoutRes = await logout()
      if (logoutRes.code != 0) {
        console.log("修改密码-登出失败:", logoutRes.message)
      }
      // 移除缓存
      localStorage.removeItem(LocalStorageEnum.JWT)
      localStorage.removeItem(LocalStorageEnum.LOGIN_USER)
      loginUser.value = null
      await router.push({
        name: "login"
      })
      Message.success("请重新登录")
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  } finally {
    passwordFormData.value = {
      oldPassword: "",
      newPassword: "",
      checkPassword: ""
    }
  }
}

const isCheckinFlag = ref(false)

const handleCheckin = async () => {
  try {
    const res = await handleDailyCheckin()
    if (res.code == 0) {
      Message.success(res.message)
      await init()
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}

const init = async () => {
  try {
    // 查询我的用户信息
    const res = await getMyUserDetail()
    if (res.code == 0) {
      userDetail.value = res.data
    } else {
      Message.error(res.message)
    }
    // 查询今日是否签到
    const isCheckinRes = await isCheckin()
    if (isCheckinRes.code == 0) {
      isCheckinFlag.value = isCheckinRes.data
    } else {
      Message.error(isCheckinRes.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
init()
</script>

<style scoped lang="scss">

</style>