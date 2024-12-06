<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      修改用户邮箱
    </template>
    <a-form :model="formData" layout="vertical">
      <a-form-item field="email" label="邮箱">
        <a-input v-model="formData.email" placeholder="请输入邮箱"/>
      </a-form-item>
      <a-form-item field="verificationCode" label="验证码">
        <a-input v-model="formData.verificationCode" placeholder="请输入验证码" />
        <a-button style="margin-left: 10px" type="primary" :disabled="isActive" @click="handleSend">{{ isActive? `请${time} 秒后重新获取` : '点击发送验证码' }}</a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import {Message} from "@arco-design/web-vue";
import {updateUser, updateUserEmail} from "../../../api/user";
import {listRole} from "../../../api/role";
import FileUpload from "../../../components/FileUpload.vue";
import {FileUploadBizEnum} from '../../../constants';
import {sendVerificationCode} from "../../../api/system";
import {useIntervalFn} from "@vueuse/core";
import {VerificationCodeBizEnum} from "../../../constants/index";

const props = defineProps(["visible"])

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.UserEmailUpdateParams>({
  email: "",
  verificationCode: ""
})

const handleOk = async () => {
  try {
    const res = await updateUserEmail(formData.value)
    if (res.code == 0) {
      // 子组件触发父组件事件
      emit("update:visible", false)
      Message.success(res.message)
      emit("ok")
      formData.value = {
        email: "",
        verificationCode: ""
      }
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
    email: "",
    verificationCode: ""
  }
}

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
    const res = await sendVerificationCode(formData.value.email, VerificationCodeBizEnum.EMAIL_UPDATE)
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

</script>

<style scoped>

</style>