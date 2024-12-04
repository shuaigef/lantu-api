<template>
  <a-upload
      :custom-request="customRequest"
      :limit="limit"
      image-preview
      :list-type="props.listType"
      v-model:fileList="fileList"
      @before-upload="beforeUpload"
  >
  </a-upload>
</template>

<script setup lang="ts">
import {uploadFile} from "../api/file";
import {computed, defineEmits, defineProps, ref, watch, withDefaults} from "vue";
import {FileItem, Message, Modal, RequestOption} from "@arco-design/web-vue";
import {COS_HOST} from "../constants/index";

const props = withDefaults(
    defineProps<{
      biz: string,
      fileUrl: string,
      limit?: number,
      listType?: 'text' | 'picture' | 'picture-card'
    }>(),
    {
      biz: "",
      fileUrl: "",
      limit: 0,
      listType: "text"
    }
)

const emit = defineEmits(["update:fileUrl"])

const fileList = ref<FileItem[]>([])
watch(
    () => [props.fileUrl, props.listType],
    ([newFileUrl, newListType]) => {
      if (newFileUrl && (newListType == 'picture' || newListType == 'picture-card')) {
        fileList.value = [
          {
            uid: Date.now().toString(),
            name: "Uploaded File",
            url: COS_HOST + newFileUrl,
            status: "success",
          }
        ]
      }
      console.log("fileList: ", fileList.value)

    },
    {
      immediate: true
    }
)

const beforeUpload = (file: File): boolean => {
  // todo 上传文件校验

  return true
}

const customRequest = async (option: RequestOption) => {
  const {onProgress, onError, onSuccess, fileItem, name} = option

  try {
    const res = await uploadFile(props.biz, fileItem.file, (progressEvent) => {
      const percent = (progressEvent.loaded / progressEvent.total) * 100
      console.log("percent", percent)
      onProgress(percent)
    })
    if (res.code == 0) {
      emit("update:fileUrl", res.data)
      onSuccess(res.data)
    } else {
      Message.error(res.message)
      onError(res.message)
    }
  } catch (error) {
    onError(error)
  }
}
</script>

<style scoped lang="scss">

</style>