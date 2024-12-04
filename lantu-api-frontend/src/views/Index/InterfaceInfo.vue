<template>
  <div class="main">
    <div class="info">
      <a-descriptions size="large" :title="interfaceInfo.name" :column="1">
        <a-descriptions-item label="接口状态">{{ interfaceInfo.status ? "开启" : "关闭" }}</a-descriptions-item>
        <a-descriptions-item label="描述">{{ interfaceInfo.description }}</a-descriptions-item>
        <a-descriptions-item label="请求地址">{{ interfaceInfo.url }}</a-descriptions-item>
        <a-descriptions-item label="请求方法">{{ interfaceInfo.method }}</a-descriptions-item>
        <a-descriptions-item label="请求参数">
          <highlightjs language="json" :code="JSON.stringify(interfaceInfo.requestParams, null, 2)"></highlightjs>
        </a-descriptions-item>
        <a-descriptions-item label="请求头">
          <highlightjs language="json" :code="JSON.stringify(interfaceInfo.requestHeader, null, 2)"></highlightjs>
        </a-descriptions-item>
        <a-descriptions-item label="响应头">
          <highlightjs language="json" :code="JSON.stringify(interfaceInfo.responseHeader, null, 2)"></highlightjs>
        </a-descriptions-item>
        <a-descriptions-item label="调用次数">{{ interfaceInfo.totalNum }}</a-descriptions-item>
        <a-descriptions-item label="消耗积分">{{ interfaceInfo.consumePoints }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ interfaceInfo.createTime }}</a-descriptions-item>
        <a-descriptions-item label="更新时间">{{ interfaceInfo.updateTime }}</a-descriptions-item>
      </a-descriptions>
    </div>
    <div v-if="interfaceInfo.status">
      <div class="interface-debug">
        <a-card :bordered="true" title="在线测试">
          <template #extra>
            <a-button type="primary" size="medium" @click="onSubmit">调用</a-button>
          </template>
          <div style="width: 100%; display: flex; flex-direction: column">
            <a-table style="width: 100%" :columns="columns" :data="data" :pagination="false">
              <template #fieldName="{ record }">
                <a-input v-model="record.fieldName" />
              </template>
              <template #value="{ record }">
                <a-input v-model="record.value" />
              </template>
              <template #operation="{ record, rowIndex }">
                <a-link type="text" status="danger" @click="handleDeleteButton(rowIndex)">删除</a-link>
              </template>
            </a-table>
            <a-button style="margin-top: 20px" @click="addRequestParams">添加一行数据</a-button>
          </div>
        </a-card>
      </div>
      <div class="interface-result">
        <a-card :bordered="true" title="返回结果">
          <highlightjs language="json" :code="JSON.stringify(resultRes, null, 2)"></highlightjs>
        </a-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {getInterfaceInfoById, invokeInterfaceInfo} from "../../api/interfaceInfo";
import {Message} from "@arco-design/web-vue";
import {useRoute} from "vue-router";

const route = useRoute()
const interfaceId = ref<string>(route.params.id + '')

// region 请求参数构造
const columns = [
  {
    title: '参数名称',
    dataIndex: 'fieldName',
    slotName: 'fieldName'
  },
  {
    title: '参数值',
    dataIndex: 'value',
    slotName: 'value'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    slotName: 'operation'
  },
]

const interfaceInfoInvokeParams = ref<API.InterfaceInfoInvokeParams>({
  id: interfaceId.value,
  requestParams: []
})
const data = ref<API.Field[]>(interfaceInfoInvokeParams.value.requestParams)

const addRequestParams = () => {
  let item: API.Field = {
    fieldName: "",
    value: ""
  }
  data.value.push(item)
}

const handleDeleteButton = (rowIndex: number) => {
  data.value.splice(rowIndex, 1)
}

const test = () => {
  console.log("1111111", interfaceInfoInvokeParams.value)
}
// endregion

// region 查询
const interfaceInfo = ref<API.InterfaceInfo>()
/**
 * 获取接口信息
 */
const getInterface = async () => {
  try {
    const res = await getInterfaceInfoById(interfaceId.value)
    if (res.code === 0) {
      interfaceInfo.value = res.data
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
getInterface()
// endregion

// region 模拟调用
const resultRes = ref({})

const onSubmit = async () => {
  try {
    const res = await invokeInterfaceInfo(interfaceInfoInvokeParams.value)
    if (res.code === 0) {
      Message.success("调用接口成功")
      resultRes.value = res.data
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}

// endregion

</script>

<style scoped lang="scss">
.main {
  margin: 40px 20px;
  .info {
    //background-color: #535bf2;
  }
  .interface-debug {
    margin-top: 20px;
  }
  .interface-result {
    margin-top: 20px;
  }
}
</style>