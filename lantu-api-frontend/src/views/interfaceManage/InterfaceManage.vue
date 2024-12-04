<template>
  <div class="main">
    <div class="table-search">
      <a-form :model="listInterfaceInfoParams">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item field="name" label="接口名称" label-col-flex="100px">
              <a-input v-model="listInterfaceInfoParams.name" placeholder=请输入接口名称 />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item field="status" label="接口状态" label-col-flex="80px">
              <a-select defaultValue="不限" v-model="listInterfaceInfoParams.status">
                <a-option :value="0">停用</a-option>
                <a-option :value="1">启用</a-option>
                <a-option :value="999">不限</a-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="7" :offset="1">
            <a-space size="medium">
              <a-button @click="onClear">清空</a-button>
              <a-button type="primary" @click="onSearch">搜索</a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="tool-bar">
      <a-space size="medium">
        <a-button type="primary" @click="addModalVisible = true">新建</a-button>
        <a-button type="primary" status="danger" v-show="selectedKeys.length > 0" @click="handleDeleteBatch">删除</a-button>
      </a-space>
      <AddInterfaceModal v-model:visible="addModalVisible" @ok="handleAddOk"/>
    </div>

    <a-table :columns="columns" :data="interfaceInfoList" :bordered="false"
             rowKey="id" :rowSelection="rowSelection" v-model:selectedKeys="selectedKeys"
             :scroll="{
               x: 2000
             }"
             :pagination="pagination"
             @pageChange="onPageChange"
             @pageSizeChange="onPageSizeChange"
    >
      <template #name="{ record }">
        <a-link type="text" @click="toInterfaceInfo(record)">{{ record.name }}</a-link>
      </template>
      <template #requestParams="{ record }">
        {{ record.requestParams }}
<!--        <highlightjs language="json" :code="JSON.stringify(record.requestParams, null, 2)"></highlightjs>-->
      </template>
      <template #requestHeader="{ record }">
        {{ record.requestHeader }}
        <!--        <highlightjs language="json" :code="JSON.stringify(record.requestHeader, null, 2)"></highlightjs>-->
      </template>
      <template #responseHeader="{ record }">
        {{ record.responseHeader }}
        <!--        <highlightjs language="json" :code="JSON.stringify(record.responseHeader, null, 2)"></highlightjs>-->
      </template>
      <template #status="{ record }">
        {{ record.status == 0 ? "关闭" : "开启" }}
      </template>
      <template #operation="{ record }">
        <a-link type="text" @click="handleUpdateButton(record)">修改</a-link>
        <a-divider direction="vertical"/>
        <a-link v-show="record.status == 0" type="text" @click="handleOnline(record)">发布</a-link>
        <a-link v-show="record.status == 1" type="text" @click="handleOffline(record)">下线</a-link>
        <a-divider direction="vertical"/>
        <a-link type="text" status="danger" @click="handleDeleteButton(record)">删除</a-link>
      </template>
    </a-table>
    <UpdateInterfaceModal v-model:visible="updateModalVisible"  :updateFormData="updateFormData" @ok="handleUpdateOk"/>
  </div>

</template>

<script setup lang="ts">
import {
  deleteBatchInterfaceInfo,
  deleteInterfaceInfo,
  listInterfaceInfoByPage,
  offlineInterfaceInfo,
  onlineInterfaceInfo
} from "../../api/interfaceInfo.ts";
import {Message} from "@arco-design/web-vue";
import {ref} from "vue";
import AddInterfaceModal from "./components/AddInterfaceModal.vue";
import UpdateInterfaceModal from "./components/UpdateInterfaceModal.vue";
import {useRouter} from "vue-router";

const columns = [
  {
    title: '接口名称',
    dataIndex: 'name',
    align: 'center',
    slotName: 'name',
    fixed: 'left'
  },
  {
    title: '接口描述',
    dataIndex: 'description',
    align: 'center'
  },
  {
    title: '地址',
    dataIndex: 'url',
    align: 'center'
  },
  {
    title: '请求参数',
    dataIndex: 'requestParams',
    align: 'left',
    slotName: 'requestParams',
  },
  {
    title: '请求头',
    dataIndex: 'requestHeader',
    align: 'left',
    slotName: 'requestHeader'
  },
  {
    title: '响应头',
    dataIndex: 'responseHeader',
    align: 'left',
    slotName: 'responseHeader'
  },
  {
    title: '状态',
    dataIndex: 'status',
    align: 'center',
    slotName: 'status'
  },
  {
    title: '请求方法',
    dataIndex: 'method',
    align: 'center'
  },
  {
    title: '调用次数',
    dataIndex: 'totalNum',
    align: 'center'
  },
  {
    title: '消耗积分',
    dataIndex: 'consumePoints',
    align: 'center'
  },
  {
    title: '创建人',
    dataIndex: 'userId',
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    slotName: 'operation',
    width: '200',
    fixed: 'right'
  },
];

// 选择的行
const selectedKeys = ref<string[]>([])
// 表格行选择配置
const rowSelection = ref({
  type: 'checkbox',
  showCheckedAll: true
})

const router = useRouter()

const toInterfaceInfo = (record: API.InterfaceInfo) => {
  router.push({ name: 'interfaceInfo', params: { id: record.id }})
}

// region 查询
/**
 * 接口信息
 */
const interfaceInfoList = ref<API.InterfaceInfo[]>([])
/**
 * 默认分页配置
 */
const defaultPagination = {
  current: 1,
  pageSize: 10,
}
/**
 * 分页信息
 */
const pagination = ref({
  showTotal: true,
  total: interfaceInfoList.value.length,
  current: defaultPagination.current,
  pageSize: defaultPagination.pageSize,
})
/**
 * 表格分页发生改变时触发
 */
const onPageChange = (page: number) => {
  selectedKeys.value = []
  pagination.value.current = page;
  listInterface()
}
/**
 * 表格每页数据数量发生改变时触发
 */
const onPageSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize;
  listInterface()
}
/**
 * 获取接口信息请求参数
 */
const listInterfaceInfoParams = ref<API.ListInterfaceInfoParams>({
  name: "",
  status: 999,
  current: pagination.value.current,
  pageSize: pagination.value.pageSize,
})
/**
 * 搜索
 */
const onSearch = () => {
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listInterface()
}
/**
 * 清空搜索条件
 */
const onClear = () => {
  listInterfaceInfoParams.value.name = ""
  listInterfaceInfoParams.value.status = 999
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listInterface()
}
/**
 * 获取接口信息
 */
const listInterface = async () => {
  try {
    listInterfaceInfoParams.value.current = pagination.value.current
    listInterfaceInfoParams.value.pageSize = pagination.value.pageSize
    const res = await listInterfaceInfoByPage(listInterfaceInfoParams.value)
    if (res.code === 0) {
      console.log("listInterfaceInfoByPage", res.data);
      interfaceInfoList.value = res.data.records
      pagination.value.total = res.data.total
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
listInterface()
// endregion

// region 新建
/**
 * 是否显示新增接口信息对话框
 */
const addModalVisible = ref(false)
/**
 * 新增接口信息对话框确认事件
 */
const handleAddOk = () => {
  onClear()
}
// endregion

// region 修改
/**
 * 是否显示修改接口信息对话框
 */
const updateModalVisible = ref(false)
/**
 * 修改的接口信息
 */
const updateFormData = ref<API.InterfaceInfoUpdateParams>()
/**
 * 修改按钮点击事件
 * @param record
 */
const handleUpdateButton = (record: API.InterfaceInfo) => {
  updateModalVisible.value = true
  updateFormData.value = {
    ...record
  }
}
/**
 * 修改接口信息对话框确认事件
 */
const handleUpdateOk = () => {
  onClear()
}
// endregion

// region 删除
/**
 * 点击表格行删除按钮事件
 * @param record
 */
const handleDeleteButton = async (record: API.InterfaceInfo) => {
  try {
    const res = await deleteInterfaceInfo(record.id)
    if (res.code == 0) {
      Message.success(res.message)
      onClear()
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
/**
 * 批量删除
 */
const handleDeleteBatch = async () => {
  try {
    const res = await deleteBatchInterfaceInfo(selectedKeys.value)
    if (res.code == 0) {
      Message.success(res.message)
      onClear()
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
// endregion

// region 发布下线
/**
 * 发布
 * @param record
 */
const handleOnline = async (record: API.InterfaceInfo) => {
  try {
    const res = await onlineInterfaceInfo(record.id)
    if (res.code == 0) {
      Message.success(res.message)
      onClear()
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
/**
 * 下线
 * @param record
 */
const handleOffline = async (record: API.InterfaceInfo) => {
  try {
    const res = await offlineInterfaceInfo(record.id)
    if (res.code == 0) {
      Message.success(res.message)
      onClear()
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
  .table-search {

  }
  .tool-bar {
    margin-bottom: 20px;
  }
}
</style>
