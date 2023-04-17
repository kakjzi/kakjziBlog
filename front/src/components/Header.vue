<template>
  <el-header class="header">
    <el-menu mode="horizontal" router :default-active="activeMenuItem">
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">글 작성</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

// 현재 경로를 가져옵니다.
const route = useRoute();

// 현재 경로에 따라 활성화된 메뉴 아이템을 계산합니다.
const activeMenuItem = computed(() => {
    return route.path === "/" ? "/" : "/write";
});
</script>

<style scoped>
.header {
  padding: 0;
}
</style>