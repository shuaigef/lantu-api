<template>
  <div class="main">
    <div class="search">
      <a-input-search
          class="input-search"
          v-model="listInterfaceInfoParams.name"
          placeholder="请输入搜索内容"
          search-button
          size="large"
          @search="onSearch"
      >
        <template #button-icon>
          <icon-search />
        </template>
        <template #button-default>
          搜索
        </template>
      </a-input-search>
    </div>
    <div class="content">
      <a-row :gutter="[24, 24]">
        <a-col :span="6" v-for="(item) in interfaceInfoList" :key="item.id">
          <a-card class="card-demo" hoverable @click="toInterfaceInfo(item)">
            <template #cover>
              <div
                  :style="{
                  height: '204px',
                  overflow: 'hidden',
                }"
              >
                <img
                    :style="{ width: '100%', transform: 'translateY(-20px)' }"
                    alt="dessert"
                    :src="item.interfaceAvatar ? COS_HOST + item.interfaceAvatar : 'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp'"
                />
              </div>
            </template>
            <a-card-meta :title="item.name">
              <template #description>
                {{ item.description }}
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
      </a-row>
    </div>
    <div class="pagination">
      <a-pagination
          :total="pagination.total"
          :showTotal="pagination.showTotal"
          v-model:current="pagination.current"
          v-model:page-size="pagination.pageSize"
          size="large"
          @change="onPageChange"
          @pageSizeChange="onPageSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {listInterfaceInfoByPage} from "../../api/interfaceInfo";
import {Message} from "@arco-design/web-vue";
import {useRouter} from "vue-router";
import {COS_HOST} from '../../constants';

const router = useRouter()

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
  pageSize: 12,
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

// region 跳转到接口文档
const toInterfaceInfo = (record: API.InterfaceInfo) => {
  router.push({ name: 'interfaceInfo', params: { id: record.id }})
}
// endregion




</script>

<style scoped lang="scss">
.main {
  margin: 40px 20px;
  .search {
    display: flex;
    justify-content: center;
    margin-bottom: 40px;
    .input-search {
      width: 550px;
    }

  }
  .content {
    .card-demo {
      transition-property: all;
    }
    .card-demo:hover {
      transform: translateY(-4px);
    }
  }
  .pagination {
    display: flex;
    justify-content: end;
    margin-top: 20px;
  }
}
</style>
