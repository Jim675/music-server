<!--
 * @description: 首页显示歌手和歌曲列表的可复用的组件
 * @author: qw Lu
 * @date: 2023-03-07 21:31:46
-->

<template>
  <div class="play-list">
    <div class="play-title" v-if="title">{{ title }}</div>
    <ul class="play-body">
      <li class="card-frame" v-for="(item, index) in playList" :key="index">
        <div class="card">
          <el-image class="card-img" fit="contain" :src="attachImageUrl(item.pic)" />
          <!-- 上一层模糊的播放图标罩 -->
          <div class="mask" @click="goAblum(item)">
            <!-- 显示图标的功能专门做成了组件 -->
            <yin-icon class="mask-icon" :icon="BOFANG"></yin-icon>
          </div>
        </div>
        <p class="card-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, toRefs } from "vue";
import YinIcon from "@/components/layouts/Icon.vue";
import mixin from "@/mixins/mixin";
import { Icon } from "@/enums";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    YinIcon,
  },
  props: {
    title: String,
    playList: Array,
    // path和路由跳转有关
    path: String,
  },
  setup(props) {
    // 解构赋值
    const { proxy } = getCurrentInstance();
    const { routerManager } = mixin();
    // 变成响应式的数据
    const { path } = toRefs(props);

    function goAblum(item) {
      // 传入 歌单 具体信息
      proxy.$store.commit("setSongDetails", item);
      routerManager(path.value, { path: `/${path.value}/${item.id}` });
    }

    return {
      BOFANG: Icon.BOFANG,
      goAblum,
      attachImageUrl: HttpManager.attachImageUrl,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.play-list {
  padding: 0 1rem;

  .play-title {
    height: 60px;
    line-height: 60px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    color: $color-black;
    box-sizing: border-box;
  }

  .play-body {
    @include layout(flex-start, stretch, row, wrap);
  }
}

.card-frame {
  .card {
    position: relative;
    height: 0;
    padding-bottom: 100%;
    overflow: hidden;
    border-radius: 5px;

    .card-img {
      width: 100%;
      transition: all 0.4s ease;
    }
  }

  .card-name {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    margin: 0.5rem 0;
  }

  &:hover .card-img {
    transform: scale(1.2);
  }
}

.mask {
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 5px;
  background-color: rgba(52, 47, 41, 0.4);
  @include layout(center, center);
  transition: all 0.3s ease-in-out;
  opacity: 0;

  .mask-icon {
    @include icon(2em, rgba(240, 240, 240, 1));
  }

  &:hover {
    opacity: 1;
    cursor: pointer;
  }
}

@media screen and (min-width: $sm) {
  .card-frame {
    width: 18%;
    margin: 0.5rem 1%;
  }
}

@media screen and (max-width: $sm) {
  .card-frame {
    width: 46%;
    margin: 0.5rem 2%;
  }
}
</style>
