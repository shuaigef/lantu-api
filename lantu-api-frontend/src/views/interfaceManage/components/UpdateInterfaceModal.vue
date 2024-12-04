<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      修改接口信息
    </template>
    <a-form :model="formData" layout="vertical">
      <a-form-item field="name" label="接口名称">
        <a-input v-model="formData.name" placeholder="请输入接口名称"/>
      </a-form-item>
      <a-form-item field="description" label="描述">
        <a-textarea v-model="formData.description" placeholder="请输入描述" allow-clear/>
      </a-form-item>
      <a-form-item field="interfaceAvatar" label="接口头像">
        <FileUpload :biz="FileUploadBizEnum.INTERFACE_AVATAR" listType="picture-card" :limit="1" v-model:fileUrl="formData.interfaceAvatar" />
      </a-form-item>
      <a-form-item field="consumePoints" label="消耗积分">
        <a-input-number mode="button" v-model="formData.consumePoints" placeholder="请输入排序" />
      </a-form-item>
      <a-form-item field="method" label="请求方法">
        <a-select v-model="formData.method" placeholder="请输入请求方法">
          <a-option value="GET">GET</a-option>
          <a-option value="POST">POST</a-option>
          <a-option value="PUT">PUT</a-option>
          <a-option value="DELETE">DELETE</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="url" label="url">
        <a-input v-model="formData.url" placeholder="请输入url"/>
      </a-form-item>
      <a-form-item field="requestParams" label="请求参数">
        <AddRequestParamsFieldTable v-model:data="formData.requestParams" />
      </a-form-item>
      <a-form-item field="requestHeader" label="请求头">
        <AddFieldTable v-model:data="formData.requestHeader" />
      </a-form-item>
      <a-form-item field="responseHeader" label="响应头">
        <AddFieldTable v-model:data="formData.responseHeader" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, ref, watch} from "vue";
import {Message} from "@arco-design/web-vue";
import {addInterfaceInfo, updateInterfaceInfo} from "../../../api/interfaceInfo";
import AddRequestParamsFieldTable from "../../../components/AddRequestParamsFieldTable.vue";
import AddFieldTable from "../../../components/AddFieldTable.vue";
import {FileUploadBizEnum} from '../../../constants';
import FileUpload from "../../../components/FileUpload.vue";

const props = defineProps<{
  visible: boolean,
  updateFormData: API.InterfaceInfoUpdateParams
}>()

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.InterfaceInfoUpdateParams>({
  id: "",
  name: "",
  description: "",
  interfaceAvatar: "",
  consumePoints: 0,
  url: "",
  method: "",
  requestParams: [],
  requestHeader: [],
  responseHeader: []
})

watch(
    () => props.updateFormData,
    (newValue) => {
      formData.value = JSON.parse(JSON.stringify(newValue))
      console.log('1123123', formData.value)
    }
)

const handleOk = async () => {
  try {
    const res = await updateInterfaceInfo(formData.value)
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
    name: "",
    description: "",
    interfaceAvatar: "",
    consumePoints: 0,
    url: "",
    method: "",
    requestParams: [],
    requestHeader: [],
    responseHeader: []
  }
}
</script>

<style scoped>

</style>