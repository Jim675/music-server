<template>
<!--  <div class="control-button">-->
<!--    <svg class="icon" aria-hidden="true">-->
<!--      <use xlink:href="#icon-bofanganniu"></use>-->
<!--    </svg>-->
<!--  </div>-->
  <audio controls="controls" preload="true" v-if="true" :ref="player" :src="attachImageUrl(url)" @canplay="startPlay" @ended="ended"></audio>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, watch, ref } from "vue";
import { useStore } from "vuex";
import { HttpManager } from "@/api";
import {
  VideoPlay
} from '@element-plus/icons-vue'
import {onBeforeRouteLeave} from "vue-router";
import login from "@/views/Login.vue";

export default defineComponent({
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const divRef = ref<HTMLAudioElement>();
    const player = (el) => {
      divRef.value = el;
    };

    const url = computed(() => store.getters.url); // 音乐链接
    const isPlay = computed(() => store.getters.isPlay); // 播放状态
    // 监听播放还是暂停
    watch(isPlay, () => {
      togglePlay();
    });

    // 开始/暂停
    function togglePlay() {
      isPlay.value ? divRef.value.play() : divRef.value.pause();
    }

    // 获取歌曲链接后准备播放
    function startPlay() {
      console.log("startPlay")
      divRef.value.play();
    }
    // 音乐播放结束时触发
    function ended() {
      proxy.$store.commit("setIsPlay", false);
    }
    return {
      url,
      isPlay,
      player,
      startPlay,
      ended,
      VideoPlay,
      attachImageUrl: HttpManager.attachImageUrl,
    };
  },
});
</script>

<style>
audio {
  display: none;
}
.control-button {
  position: absolute;
  bottom: 0;
  font-size: 32px;
  z-index: 100;
  width: 80px;
  height: 80px;
  left: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.icon {
  width: 1em;
  height: 1em;
  color: #252222;
  fill: currentColor;
  overflow: hidden;
}
</style>
