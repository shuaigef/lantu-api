<template>
    <a-drawer :width="340" v-model:visible="props.visible" @ok="handleOk" @cancel="handleCancel" unmountOnClose>
      <template #title>
        所拥有的权限
      </template>
      <a-tree
          size="large"
          :block-node="true"
          :checkable="true"
          :defaultExpandAll="true"
          :fieldNames="{
            key: 'id',
            title: 'name',
            children: 'children'
          }"
          v-model:checkedKeys="checkedKeys"
          v-model:halfCheckedKeys="halfCheckedKeys"
          :data="treeData"
      />
    </a-drawer>
</template>

<script setup lang="ts">
import {computed, defineEmits, defineProps, ref, watch} from "vue";
import {Message} from "@arco-design/web-vue";
import {roleBindAuthority} from "../../../api/authority";

const props = defineProps<{
  visible: boolean,
  authRoleId: string,
  authTreeData: API.Authority[]
}>()

const emit = defineEmits(["update:visible"])

const treeData = ref<API.Authority[]>()
const checkedKeys = ref<string[]>([])
const halfCheckedKeys = ref<string[]>([])

const authorityUpdateParams = computed<API.RoleAuthorityUpdateParams>(() => ({
  roleId: props.authRoleId,
  authorityIds: checkedKeys.value.concat(halfCheckedKeys.value),
}))
const handleOk = async () => {
  console.log("修改权限", authorityUpdateParams.value)
  try {
    const res = await roleBindAuthority(authorityUpdateParams.value)
    if (res.code == 0) {
      // 子组件触发父组件事件
      emit("update:visible", false)
      Message.success(res.message)
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}

const handleCancel = () => {
  emit("update:visible", false)
}

const initCheckedAndHalfCheckedKeys = (tree: API.Authority[]) => {
  const checked: string[] = [];
  const halfChecked: string[] = [];

  const traverse = (nodes: API.Authority[]): { isAllChecked: boolean; isSomeChecked: boolean } => {
    let isAllChecked = true;
    let isSomeChecked = false;

    for (const node of nodes) {
      if (node.children && node.children.length > 0) {
        const { isAllChecked: childAll, isSomeChecked: childSome } = traverse(node.children);

        if (childAll) {
          checked.push(node.id); // 子节点全选中，父节点完全选中
        } else if (childSome) {
          halfChecked.push(node.id); // 子节点部分选中，父节点半选
        }
        isAllChecked = isAllChecked && childAll;
        isSomeChecked = isSomeChecked || childSome;
      } else {
        if (node.check) {
          checked.push(node.id); // 叶子节点选中
        } else {
          isAllChecked = false; // 如果有一个未选中，父节点不能是全选
        }
        isSomeChecked = isSomeChecked || !!node.check;
      }
    }

    return { isAllChecked, isSomeChecked };
  };

  traverse(tree);
  checkedKeys.value = checked;
  halfCheckedKeys.value = halfChecked;

  console.log("完全选中的节点:", checkedKeys.value);
  console.log("半选中的节点:", halfCheckedKeys.value);
};

watch(
    () => props.authTreeData,
    async (newValue) => {
      treeData.value = newValue
      initCheckedAndHalfCheckedKeys(newValue)
    }
)

</script>

<style scoped>

</style>