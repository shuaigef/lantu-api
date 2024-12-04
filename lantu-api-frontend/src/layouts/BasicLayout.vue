<template>
  <a-layout class="layout-demo">
    <a-layout-sider
        hide-trigger
        collapsible
        :collapsed="collapsed"
    >
      <div class="logo">
        <div style="display: flex; justify-content: start; align-items: center">
          <img style="height: 32px;" :src="LOGO_URL"/>
          <span v-if="!collapsed" style="font-size: 19px;font-weight: 600;min-width: 150px">
            蓝图API开放平台
          </span>
        </div>
      </div>
      <a-menu
          :defaultSelectedKeys="defaultSelectedKeys"
          :style="{ width: '100%' }"
          @menuItemClick="toPage"
      >
        <template v-for="menu in loginUser.authorityList" :key="menu.routePath">
          <a-menu-item v-if="(!menu.children || menu.children.length === 0)" :key="menu.routePath">
            <icon-font v-if="menu.menuIcon" :type="menu.menuIcon" :size="14" />{{ menu.name }}
          </a-menu-item>

          <a-sub-menu v-else-if="menu.children && menu.children.length >= 0" :key="menu.routePath">
            <template #title>
              <icon-font v-if="menu.menuIcon" :type="menu.menuIcon" :size="14" />{{ menu.name }}
            </template>
            <a-menu-item v-for="child in menu.children" :key="child.routePath">
              {{ child.name }}
            </a-menu-item>
          </a-sub-menu>
        </template>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="padding: 0 20px 0 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center">
          <a-button shape="round" @click="onCollapse">
            <IconCaretRight v-if="collapsed" />
            <IconCaretLeft v-else />
          </a-button>
          <div>
            <a-dropdown>
              <div>
                <a-avatar :imageUrl="loginUser ? COS_HOST + loginUser.userInfo.userAvatar : ''" />
                <span style="margin-left: 12px">{{ loginUser.userInfo.nickname }}</span>
              </div>
              <template #content>
                <a-doption position="bl" @click="toAccountSetting">
                  <template #icon>
                    <icon-font type="icon-account-settings" :size="14" />
                  </template>
                  <template #default>账号设置</template>
                </a-doption>
                <a-doption position="bl" @click="toLogout">
                  <template #icon>
                    <icon-import />
                  </template>
                  <template #default>登出</template>
                </a-doption>
              </template>
            </a-dropdown>
          </div>
        </div>
      </a-layout-header>
      <a-layout style="padding: 0 24px;">
        <a-breadcrumb :style="{ margin: '16px 0' }">
          <a-breadcrumb-item>{{  route.meta.title }}</a-breadcrumb-item>
        </a-breadcrumb>
        <a-layout-content>
          <router-view/>
        </a-layout-content>
        <a-layout-footer>蓝图 API 开放平台</a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {
  IconSettings,
  IconCaretRight,
  IconCaretLeft
} from '@arco-design/web-vue/es/icon';
import {useRoute, useRouter} from "vue-router";
import {LocalStorageEnum} from "../constants";
import {useSystemStore} from "../store";
import {storeToRefs} from "pinia";
import {Icon, Message} from '@arco-design/web-vue';
import {COS_HOST, LOGO_URL} from '../constants';
import {updatePersonalPassword} from "../api/user";
import {logout} from "../api/system";

const IconFont = Icon.addFromIconFontCn({ src: 'https://at.alicdn.com/t/c/font_4738168_ijmht3p7999.js' });

const systemStore = useSystemStore()
const { loginUser } = storeToRefs(systemStore)

const collapsed = ref(false)
const onCollapse = () => {
  collapsed.value = !collapsed.value
}

const defaultSelectedKeys = ref<string[]>(["/interfaceList"])

const route = useRoute();
const router = useRouter();

const toPage = (key) => {
  defaultSelectedKeys.value[0] = key
  router.push({
    path: key
  })
}
toPage(defaultSelectedKeys.value[0])

const toLogout = async () => {
  try {
    const res = await logout()
    Message.success(res.message)
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  } finally {
    localStorage.removeItem(LocalStorageEnum.JWT)
    localStorage.removeItem(LocalStorageEnum.LOGIN_USER)
    loginUser.value = null
    await router.push({
      name: "login"
    })
  }
}
const toAccountSetting = () => {
  router.push({
    name: "accountSetting"
  })
}

</script>

<style scoped lang="scss">
.layout-demo {
  height: 100vh;
  background: var(--color-fill-2);
  border: 1px solid var(--color-border);
  :deep(.arco-layout-sider) {
    .logo {
      height: 32px;
      margin: 12px 8px;
      background: rgba(255, 255, 255, 0.2);
    }
  }
  :deep(.arco-layout-sider-light) {
    .logo{
      //background: var(--color-fill-2);
    }
  }
  :deep(.arco-layout-header) {
    height: 64px;
    line-height: 64px;
    background: var(--color-bg-3);
  }
  :deep(.arco-layout-footer) {
    height: 48px;
    color: var(--color-text-2);
    font-weight: 400;
    font-size: 14px;
    line-height: 48px;
  }
  :deep(.arco-layout-content) {
    //color: var(--color-text-2);
    color: var(---color-black);
    font-weight: 400;
    font-size: 16px;
    background: var(--color-bg-3);
  }
  :deep(.arco-layout-footer) {
    display: flex;
    flex-direction: column;
    justify-content: center;
    color: var(--color-black);
    font-size: 16px;
    font-stretch: condensed;
    text-align: center;
  }
}
</style>
