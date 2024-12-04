<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      新增用户
    </template>
    <a-form :model="formData" layout="vertical">
      <a-form-item field="name" label="用户名">
        <a-input v-model="formData.username" placeholder="请输入用户名"/>
      </a-form-item>
      <a-form-item field="password" label="密码">
        <a-input-password v-model="formData.password" placeholder="请输入密码" :defaultVisibility="true" allow-clear/>
      </a-form-item>
      <a-form-item field="checkPassword" label="确认密码">
        <a-input-password v-model="formData.checkPassword" placeholder="请输入确认密码" :defaultVisibility="true" allow-clear/>
      </a-form-item>
      <a-form-item field="nickname" label="昵称">
        <a-input v-model="formData.nickname" placeholder="请输入昵称"/>
      </a-form-item>
      <a-form-item field="userProfile" label="个人简介">
        <a-textarea v-model="formData.userProfile" placeholder="请输入个人简介" allow-clear/>
      </a-form-item>
      <a-form-item field="roleId" label="角色">
        <a-select v-model="formData.roleId" placeholder="请选择角色">
          <a-option v-for="role in roleList" :key="role.id" :value="role.id">{{ role.roleName }}</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="gender" label="性别">
        <a-select v-model="formData.gender" placeholder="请选择性别">
          <a-option :value="0">未知</a-option>
          <a-option :value="1">男</a-option>
          <a-option :value="2">女</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="email" label="邮箱">
        <a-input v-model="formData.email" placeholder="请输入邮箱"/>
      </a-form-item>
      <a-form-item field="phoneNumber" label="手机号">
        <a-input v-model="formData.phoneNumber" placeholder="请输入手机号"/>
      </a-form-item>
      <a-form-item field="userAvatar" label="头像">
        <FileUpload :biz="FileUploadBizEnum.USER_AVATAR" listType="picture-card" :limit="1" v-model:fileUrl="formData.userAvatar" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, ref} from "vue";
import {Message} from "@arco-design/web-vue";
import {addUser} from "../../../api/user";
import {listRole} from "../../../api/role";
import FileUpload from "../../../components/FileUpload.vue";
import {FileUploadBizEnum} from '../../../constants';

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.UserAddParams>({
  username: "",
  password: "",
  checkPassword: "",
  nickname: "",
  userAvatar: "",
  userProfile: "",
  roleId: "",
  gender: 0,
  email: "",
  phoneNumber: ""
})

const handleOk = async () => {
  try {
    const res = await addUser(formData.value)
    if (res.code == 0) {
      // 子组件触发父组件事件
      handleCancel()
      Message.success(res.message)
      emit("ok")
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }

}

const handleCancel = () => {
  emit("update:visible", false)
  formData.value = {
    username: "",
    password: "",
    checkPassword: "",
    nickname: "",
    userAvatar: "",
    userProfile: "",
    roleId: "",
    gender: 0,
    email: "",
    phoneNumber: ""
  }
}

const roleList = ref<API.Role[]>()

const getRoleList = async () => {
  try {
    const res = await listRole()
    if (res.code == 0) {
      roleList.value = res.data
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
getRoleList()
</script>

<style scoped>

</style>