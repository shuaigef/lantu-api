<template>
  <a-modal v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel">
    <template #title>
      新增权限
    </template>
    <a-form :model="formData" layout="vertical">
      <a-form-item field="code" label="code">
        <a-input v-model="formData.code" placeholder="请输入 code" />
      </a-form-item>
      <a-form-item field="name" label="权限名称">
        <a-input v-model="formData.name" placeholder="请输入权限名称" />
      </a-form-item>
      <a-form-item field="orderNo" label="排序">
        <a-input-number mode="button" v-model="formData.orderNo" placeholder="请输入排序" />
      </a-form-item>
      <a-form-item field="parentId" label="父权限 id">
        <a-input v-model="formData.parentId" placeholder="请输入父权限 id"/>
      </a-form-item>
      <a-form-item field="authorityType" label="权限类型">
        <a-select v-model="formData.authorityType" placeholder="请选择权限类型">
          <a-option value="menu">菜单</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="routePath" label="跳转路径">
        <a-input v-model="formData.routePath" placeholder="请输入跳转路径"/>
      </a-form-item>
      <a-form-item field="hidden" label="是否隐藏菜单">
        <a-select v-model="formData.hidden" placeholder="请选择是否隐藏菜单">
          <a-option :value="0">否</a-option>
          <a-option :value="1">是</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="menuIcon" label="图标">
        <a-input v-model="formData.menuIcon" placeholder="请输入图标"/>
      </a-form-item>
      <a-form-item field="component" label="组件路径">
        <a-input v-model="formData.component" placeholder="请输入组件路径"/>
      </a-form-item>
      <a-form-item field="componentName" label="组件名称">
        <a-input v-model="formData.componentName" placeholder="请输入组件名称"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, ref} from "vue";
import {Message} from "@arco-design/web-vue";
import {addAuthority} from "../../../api/authority";

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(["ok", "update:visible"])

const formData = ref<API.AuthorityAddParams>({
  code: "",
  name: "",
  orderNo: 1,
  parentId: "0",
  authorityType: "",
  routePath: "",
  hidden: 0,
  menuIcon: "",
  component: "",
  componentName: "",
})

const handleOk = async () => {
  try {
    const res = await addAuthority(formData.value)
    if (res.code == 0) {
      // 子组件触发父组件事件
      handleCancel()
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
    code: "",
    name: "",
    orderNo: 1,
    parentId: "0",
    authorityType: "",
    routePath: "",
    hidden: 0,
    menuIcon: "",
    component: "",
    componentName: "",
  }
}
</script>

<style scoped>

</style>