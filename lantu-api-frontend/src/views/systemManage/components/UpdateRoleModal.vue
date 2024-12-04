<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      修改用户
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
import {defineEmits, defineProps, ref, watch} from "vue";
import {Message} from "@arco-design/web-vue";
import {updateUser} from "../../../api/user";
import {listRole, updateRole} from "../../../api/role";

const props = defineProps(["visible", "updateFormData"])

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.RoleUpdateParams>({
  id: "",
  roleName: "",
  roleDesc: ""
})

watch(
    () => props.updateFormData,
    (newValue) => {
      formData.value = {
        ...newValue
      }
    }
)

const handleOk = async () => {
  try {
    const res = await updateRole(formData.value)
    if (res.code == 0) {
      // 子组件触发父组件事件
      emit("update:visible", false)
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
    id: "",
    roleName: "",
    roleDesc: ""
  }
}
</script>

<style scoped>

</style>