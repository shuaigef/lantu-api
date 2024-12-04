<template>
  <div class="main">
    <div class="table-search">
      <a-form :model="queryRoleParams">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item field="roleName" label="角色名" label-col-flex="100px">
              <a-input v-model="queryRoleParams.roleName" placeholder=请输入角色名 />
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
        <a-button type="primary" v-show="loginUser.userInfo.roleId == 1" @click="handleResetAdmin">重置超级管理员权限</a-button>
      </a-space>
      <AddRoleModal v-model:visible="addModalVisible" @ok="handleAddOk"/>
    </div>

    <a-table :columns="columns" :data="userList" :bordered="false"
             rowKey="id" :rowSelection="rowSelection" v-model:selectedKeys="selectedKeys"
             :pagination="pagination"
             @pageChange="onPageChange"
             @pageSizeChange="onPageSizeChange"
    >
      <template #operation="{ record }">
        <a-button type="text" @click="handleUpdateButton(record)">修改</a-button>
        <a-divider direction="vertical"/>
        <a-button type="text" @click="handleAuthButton(record)">权限</a-button>
        <a-divider direction="vertical"/>
        <a-button type="text" status="danger" @click="handleDeleteButton(record)">删除</a-button>
      </template>
    </a-table>
    <UpdateRoleModal v-model:visible="updateModalVisible"  :updateFormData="updateFormData" @ok="handleUpdateOk"/>
  </div>
  <UpdateAuthDrawer v-model:visible="updateAuthVisible" :authRoleId="authRoleId" :authTreeData="authTreeData"/>
</template>

<script setup lang="ts">
import {Message} from "@arco-design/web-vue";
import {ref} from "vue";
import {deleteBatchRole, deleteRole, queryRoleByPage} from "../../api/role";
import UpdateRoleModal from "./components/UpdateRoleModal.vue";
import AddRoleModal from "./components/AddRoleModal.vue";
import UpdateAuthDrawer from "./components/UpdateAuthDrawer.vue";
import {getAuthorityTreeByRoleId, resetAdminAuthority} from "../../api/authority";
import {storeToRefs} from "pinia";
import {useSystemStore} from "../../store/index";

const columns = [
  {
    title: '角色名',
    dataIndex: 'roleName',
    align: 'center'
  },
  {
    title: '角色描述',
    dataIndex: 'roleDesc',
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
const userList = ref<API.Role[]>([])
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
  listRole()
}
/**
 * 表格每页数据数量发生改变时触发
 */
const onPageSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize;
  listRole()
}
/**
 * 获取接口信息请求参数
 */
const queryRoleParams = ref<API.RoleQueryParams>({
  roleName: "",
  current: pagination.value.current,
  pageSize: pagination.value.pageSize,
})
/**
 * 搜索
 */
const onSearch = () => {
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listRole()
}
/**
 * 清空搜索条件
 */
const onClear = () => {
  queryRoleParams.value.roleName = ""
  pagination.value.current = defaultPagination.current
  pagination.value.pageSize = defaultPagination.pageSize
  listRole()
}
/**
 * 获取接口信息
 */
const listRole = async () => {
  try {
    queryRoleParams.value.current = pagination.value.current
    queryRoleParams.value.pageSize = pagination.value.pageSize
    const res = await queryRoleByPage(queryRoleParams.value)
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
listRole()
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

// region 重置超级管理员权限
const systemStore = useSystemStore()
const { loginUser } = storeToRefs(systemStore)

const handleResetAdmin = async () => {
  try {
    const res = await resetAdminAuthority()
    if (res.code == 0) {
      Message.success(res.message)
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
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
const updateFormData = ref<API.RoleUpdateParams>()
/**
 * 修改按钮点击事件
 * @param record
 */
const handleUpdateButton = (record: API.Role) => {
  updateModalVisible.value = true
  updateFormData.value = {
    ...record
  }
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
    const res = await deleteRole(record.id)
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
    const res = await deleteBatchRole(selectedKeys.value)
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

// region 权限
const updateAuthVisible = ref(false)
const authTreeData = ref<API.Authority[]>()
const authRoleId = ref<string>()
const handleAuthButton = async (record: API.Role) => {
  authRoleId.value = record.id
  updateAuthVisible.value = true
  try {
    const res = await getAuthorityTreeByRoleId(record.id)
    if (res.code == 0) {
      authTreeData.value = res.data
      updateAuthVisible.value = true
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
