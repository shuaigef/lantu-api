<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      新增角色
    </template>
    <a-form :model="formData" layout="vertical">
      <a-form-item field="roleName" label="角色名">
        <a-input v-model="formData.roleName" placeholder="请输入角色名"/>
      </a-form-item>
      <a-form-item field="roleDesc" label="角色描述">
        <a-input v-model="formData.roleDesc" placeholder="请输入角色描述"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, ref} from "vue";
import {Message} from "@arco-design/web-vue";
import {addRole} from "../../../api/role";

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.RoleAddParams>({
  roleName: "",
  roleDesc: ""
})

const handleOk = async () => {
  try {
    const res = await addRole(formData.value)
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
    roleName: "",
    roleDesc: ""
  }
}

</script>

<style scoped>

</style>