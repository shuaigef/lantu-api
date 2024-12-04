<template>
  <div class="main">
    <div class="table-search">
      <a-form :model="listUserParams">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item field="username" label="用户名" label-col-flex="100px">
              <a-input v-model="listUserParams.username" placeholder=请输入用户名 />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item field="nickname" label="昵称" label-col-flex="100px">
              <a-input v-model="listUserParams.nickname" placeholder=请输入昵称 />
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
      <AddUserModal v-model:visible="addModalVisible" @ok="handleAddOk"/>
    </div>

    <a-table :columns="columns" :data="userList" :bordered="false"
             rowKey="id" :rowSelection="rowSelection" v-model:selectedKeys="selectedKeys"
             :pagination="pagination"
             @pageChange="onPageChange"
             @pageSizeChange="onPageSizeChange"
    >
      <template #gender="{ record }">
        {{ record.gender == 0 ? '未知' :  record.gender == 1 ? '男' : '女' }}
      </template>
      <template #operation="{ record }">
        <a-button type="text" @click="handleUpdateButton(record)">修改</a-button>
        <a-divider direction="vertical"/>
        <a-button type="text" status="danger" @click="handleDeleteButton(record)">删除</a-button>
      </template>
    </a-table>
    <UpdateUserModal v-model:visible="updateModalVisible"  :updateFormData="updateFormData" @ok="handleUpdateOk"/>
  </div>

</template>

<script setup lang="ts">
import {Message} from "@arco-design/web-vue";
import {ref} from "vue";
import {deleteBatchUser, deleteUser, queryUserByPage} from "../../api/user";
import AddUserModal from "./components/AddUserModal.vue";
import UpdateUserModal from "./components/UpdateUserModal.vue";

const columns = [
  {
    title: '用户名',
    dataIndex: 'username',
    align: 'center'
  },
  {
    title: '昵称',
    dataIndex: 'nickname',
    align: 'center'
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    align: 'center'
  },
  {
    title: '个人简介',
    dataIndex: 'userProfile',
    align: 'center'
  },
  {
    title: '性别',
    dataIndex: 'gender',
    align: 'center',
    slotName: 'gender'
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    align: 'center'
  },
  {
    title: '手机号',
    dataIndex: 'phoneNumber',
    align: 'center'
  },
  {
    title: '积分',
    dataIndex: 'userPoints',
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
    slotName: 'operation'
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
const userList = ref<API.User[]>([])
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
  total: userList.value.length,
  current: defaultPagination.current,
  pageSize: defaultPagination.pageSize,
})
/**
 * 表格分页发生改变时触发
 */
const onPageChange = (page: number) => {
  selectedKeys.value = []
  pagination.value.current = page;
  listUser()
}
/**
 * 表格每页数据数量发生改变时触发
 */
const onPageSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize;
  listUser()
}
/**
 * 获取接口信息请求参数
 */
const listUserParams = ref<API.UserQueryParams>({
  username: "",
  nickname: "",
  current: pagination.value.current,
  pageSize: pagination.value.pageSize,
})
/**
 * 搜索
 */
const onSearch = () => {
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listUser()
}
/**
 * 清空搜索条件
 */
const onClear = () => {
  listUserParams.value.username = ""
  listUserParams.value.nickname = ""
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listUser()
}
/**
 * 获取接口信息
 */
const listUser = async () => {
  try {
    listUserParams.value.current = pagination.value.current
    listUserParams.value.pageSize = pagination.value.pageSize
    const res = await queryUserByPage(listUserParams.value)
    if (res.code === 0) {
      userList.value = res.data.records
      pagination.value.total = res.data.total
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
listUser()
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
const updateFormData = ref<API.UserUpdateParams>()
/**
 * 修改按钮点击事件
 * @param record
 */
const handleUpdateButton = (record: API.User) => {
  updateModalVisible.value = true
  updateFormData.value = {
    ...record
  }
  console.log("update User: ", updateFormData.value)
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
const handleDeleteButton = async (record: API.User) => {
  try {
    const res = await deleteUser(record.id)
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
    const res = await deleteBatchUser(selectedKeys.value)
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
