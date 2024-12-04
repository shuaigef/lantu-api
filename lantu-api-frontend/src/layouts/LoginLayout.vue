<template>
  <a-layout>
    <a-layout-sider :width="550" :hide-trigger="true" class="">
      <div class="sider">
        <div class="logo">
          <img class="logo-img" :src="LOGO_URL">
          <div class="logo-text">蓝图API开放平台</div>
        </div>
        <div class="banner">
          <div class="banner-title">提供了各类常用 API</div>
          <div class="banner-sub-title">提高你的项目开发效率</div>
          <img class="banner-img" src="/src/assets/login-banner.png"/>

        </div>
      </div>
    </a-layout-sider>
    <a-layout-content>
      <router-view/>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import {LOGO_URL} from '../constants';

</script>

<style scoped lang="scss">
.sider {
  background: linear-gradient(163.85deg,#1d2129 0%,#00308f 100%);
  height: 100vh;
  .logo {
    position: fixed;
    top: 24px;
    left: 22px;
    z-index: 1;
    display: inline-flex;
    align-items: center;
    .logo-img {
      width: 44px;
      height: 44px;
    }
    .logo-text {
      margin-right: 4px;
      margin-left: 4px;
      color: rgb( 247,248,250 );
      font-size: 20px;
    }
  }
  .banner {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    .banner-title {
      color: rgb( 247,248,250 );
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
    }
    .banner-sub-title {
      margin-top: 8px;
      color: rgb( 134,144,156 );
      font-size: 14px;
      line-height: 22px;
    }
    .banner-img {
      width: 320px;
      margin-top: 30px;
    }
  }
}
</style>