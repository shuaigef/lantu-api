<template>
  <div class="main">
    <div class="table-search">
      <a-form :model="listAuthorityParams">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item field="code" label="code" label-col-flex="100px">
              <a-input v-model="listAuthorityParams.code" placeholder="请输入code" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item field="name" label="权限名称" label-col-flex="100px">
              <a-input v-model="listAuthorityParams.name" placeholder="请输入权限名称" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item field="authorityType" label="权限类型" label-col-flex="100px">
              <a-input v-model="listAuthorityParams.authorityType" placeholder="请输入权限类型" />
            </a-form-item>
          </a-col>
          <a-col :span="5" :offset="1">
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
      <AddAuthorityModal v-model:visible="addModalVisible" @ok="handleAddOk"/>
    </div>

    <a-table :columns="columns" :data="authorityList" :bordered="false"
             rowKey="id" :rowSelection="rowSelection" v-model:selectedKeys="selectedKeys"
             :pagination="pagination"
             :scroll="{
               x: 2000
             }"
             @pageChange="onPageChange"
             @pageSizeChange="onPageSizeChange"
    >
      <template #operation="{ record }">
        <a-button type="text" @click="handleUpdateButton(record)">修改</a-button>
        <a-divider direction="vertical"/>
        <a-button type="text" status="danger" @click="handleDeleteButton(record)">删除</a-button>
      </template>
    </a-table>
    <UpdateAuthorityModal v-model:visible="updateModalVisible"  :updateFormData="updateFormData" @ok="handleUpdateOk"/>
  </div>

</template>

<script setup lang="ts">
import {Message} from "@arco-design/web-vue";
import {ref} from "vue";
import {deleteAuthority, deleteBatchAuthority, queryAuthorityByPage} from "../../api/authority";
import AddAuthorityModal from "./components/AddAuthorityModal.vue";
import UpdateAuthorityModal from "./components/UpdateAuthorityModal.vue";

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    align: 'center',
    fixed: 'left'
  },
  {
    title: '权限名称',
    dataIndex: 'name',
    align: 'center',
    width: 200
  },
  {
    title: 'code',
    dataIndex: 'code',
    align: 'center',
    width: '250',
  },
  {
    title: '排序',
    dataIndex: 'orderNo',
    align: 'center'
  },
  {
    title: '父节点 Id',
    dataIndex: 'parentId',
    align: 'center'
  },
  {
    title: '权限类型',
    dataIndex: 'authorityType',
    align: 'center',
  },
  {
    title: '跳转路径',
    dataIndex: 'routePath',
    align: 'center',
    width: '250',
  },
  {
    title: '是否隐藏',
    dataIndex: 'hidden',
    align: 'center'
  },
  {
    title: '图标名',
    dataIndex: 'menuIcon',
    align: 'center',
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    align: 'center',
    width: '250',
  },
  {
    title: '组件名称',
    dataIndex: 'componentName',
    align: 'center',
    width: '200',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: '200',
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

// region 查询
/**
 * 接口信息
 */
const authorityList = ref<API.Authority[]>([])
/**
 * 默认分页配置
 */
const defaultPagination = {
  current: 1,
  pageSize: 20,
}
/**
 * 分页信息
 */
const pagination = ref({
  showTotal: true,
  total: authorityList.value.length,
  current: defaultPagination.current,
  pageSize: defaultPagination.pageSize,
})
/**
 * 表格分页发生改变时触发
 */
const onPageChange = (page: number) => {
  selectedKeys.value = []
  pagination.value.current = page;
  listAuthority()
}
/**
 * 表格每页数据数量发生改变时触发
 */
const onPageSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize;
  listAuthority()
}
/**
 * 获取接口信息请求参数
 */
const listAuthorityParams = ref<API.AuthorityQueryParams>({
  code: "",
  name: "",
  authorityType: "",
  current: pagination.value.current,
  pageSize: pagination.value.pageSize,
})
/**
 * 搜索
 */
const onSearch = () => {
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listAuthority()
}
/**
 * 清空搜索条件
 */
const onClear = () => {
  listAuthorityParams.value.code = ""
  listAuthorityParams.value.name = ""
  listAuthorityParams.value.authorityType = ""
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listAuthority()
}
/**
 * 获取接口信息
 */
const listAuthority = async () => {
  try {
    listAuthorityParams.value.current = pagination.value.current
    listAuthorityParams.value.pageSize = pagination.value.pageSize
    const res = await queryAuthorityByPage(listAuthorityParams.value)
    if (res.code === 0) {
      authorityList.value = res.data.records
      pagination.value.total = res.data.total
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
listAuthority()
// endregion

// region 新建
/**
 * 是否显示新增对话框
 */
const addModalVisible = ref(false)
/**
 * 新增对话框确认事件
 */
const handleAddOk = () => {
  onClear()
}
// endregion

// region 修改
/**
 * 是否显示修改对话框
 */
const updateModalVisible = ref(false)
/**
 * 修改的数据
 */
const updateFormData = ref<API.AuthorityUpdateParams>()
/**
 * 修改按钮点击事件
 * @param record
 */
const handleUpdateButton = (record: API.Authority) => {
  updateModalVisible.value = true
  updateFormData.value = {
    ...record
  }
  console.log("update Authority: ", updateFormData.value)
}
/**
 * 修改对话框确认事件
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
const handleDeleteButton = async (record: API.Authority) => {
  try {
    const res = await deleteAuthority(record.id)
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
    const res = await deleteBatchAuthority(selectedKeys.value)
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
