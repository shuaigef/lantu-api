<template>
  <div style="width: 100%; display: flex; flex-direction: column">
    <a-table style="width: 100%" :columns="columns" :data="props.data" :pagination="false">
      <template #fieldName="{ record }">
        <a-input v-model="record.fieldName" />
      </template>
      <template #type="{ record }">
        <a-input v-model="record.type" />
      </template>
      <template #description="{ record }">
        <a-input v-model="record.description" />
      </template>
      <template #operation="{ record, rowIndex }">
        <a-link type="text" status="danger" @click="handleDeleteButton(rowIndex)">删除</a-link>
      </template>
    </a-table>
    <a-button style="margin-top: 20px" @click="addRequestParams">添加一行数据</a-button>
  </div>
</template>

<script setup lang="ts">
import {defineEmits, defineProps} from "vue";

const props = defineProps<{
  data: API.RequestParamsField[]
}>()

const emit = defineEmits(["update:data"])

const columns = [
  {
    title: '参数名称',
    dataIndex: 'fieldName',
    slotName: 'fieldName'
  },
  {
    title: '参数类型',
    dataIndex: 'type',
    slotName: 'type'
  },
  {
    title: '描述',
    dataIndex: 'description',
    slotName: 'description'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    slotName: 'operation'
  },
]

const handleDeleteButton = (rowIndex: number) => {
  props.data.splice(rowIndex, 1)
  emit("update:data", props.data)
}

const addRequestParams = () => {
  let item: API.RequestParamsField = {
    fieldName: "",
    type: "",
    description: ""
  }
  props.data.push(item)
  emit("update:data", props.data)
}
</script>

<style scoped lang="scss">

</style>