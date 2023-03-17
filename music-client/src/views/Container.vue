<!--
 * @description: 以下使用三段式结构,路由跳转只改变中间main部分的结构
 * @author: qw Lu
 * @date: 2023/03/13 15:35:18
-->
<template>
  <el-container>
    <el-header>
      <yin-header></yin-header>
    </el-header>
    <el-main>
      <router-view />
      <yin-current-play></yin-current-play>
      <yin-play-bar></yin-play-bar>
      <yin-scroll-top></yin-scroll-top>
      <yin-audio></yin-audio>
    </el-main>
    <el-footer>
      <yin-footer></yin-footer>
    </el-footer>
  </el-container>
</template>

<script lang="ts" setup>
import { getCurrentInstance } from "vue";
import YinHeader from "@/components/layouts/Header.vue";
import YinCurrentPlay from "@/components/layouts/CurrentPlay.vue";
import YinPlayBar from "@/components/layouts/PlayBar.vue";
import YinScrollTop from "@/components/layouts/ScrollTop.vue";
import YinFooter from "@/components/layouts/Footer.vue";
import YinAudio from "@/components/layouts/Audio.vue";

const { proxy } = getCurrentInstance();

if (sessionStorage.getItem("dataStore")) {
  proxy.$store.replaceState(Object.assign({}, proxy.$store.state, JSON.parse(sessionStorage.getItem("dataStore"))));
}

window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("dataStore", JSON.stringify(proxy.$store.state));
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.el-container {
  min-height: calc(100% - 60px);
}
.el-header {
  padding: 0;
}
.el-main {
  padding-left: 0;
  padding-right: 0;
}
</style>
