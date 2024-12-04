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
import {COS_HOST, FileUploadBizEnum} from "../constants/index";

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
    },
    {
      immediate: true
    }
)

const beforeUpload = (file: File): boolean => {
  if (FileUploadBizEnum.USER_AVATAR == props.biz || FileUploadBizEnum.INTERFACE_AVATAR == props.biz) {
    // 文件大小不能超过 1MB
    if (file.size > 1024 * 1024) {
      Message.error("文件大小不能超过 1MB")
      return false
    }
    // 文件类型错误
    const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1)
    const imageTypeList = ["jpeg", "jpg", "svg", "png", "webg"]
    if (!imageTypeList.includes(fileSuffix)) {
      Message.error("文件类型错误")
      return false
    }
  } else {
    // biz错误
    Message.error("系统错误")
    console.log("文件上传错误-biz 不存在：", props.biz)
    return false
  }
  return true
}

const customRequest = async (option: RequestOption) => {
  const {onProgress, onError, onSuccess, fileItem, name} = option

  try {
    const res = await uploadFile(props.biz, fileItem.file, (progressEvent) => {
      const percent = (progressEvent.loaded / progressEvent.total) * 100
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