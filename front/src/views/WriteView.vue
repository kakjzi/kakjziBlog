<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const title = ref("");
const content = ref("");
const category = ref("");
const errors = ref({});

const router = useRouter();

const options = [
  {
    value: "개발",
    label: "개발",
  },
  {
    value: "일상",
    label: "일상",
  },
  {
    value: "여행",
    label: "여행",
  },
];
const write = function () {
  axios.post("/api/posts", {
        title: title.value,
        content: content.value,
        category: category.value,
      })
      .then(() => {
        router.replace({ name: "home" });
      })
      .catch((err) => {
        if (err.response && err.response.data && err.response.data.validation) {
          errors.value = err.response.data.validation;
        } else {
          alert("An error occurred. Please try again.");
        }
      });
};
</script>
<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
    <el-text v-if="errors.title" class="mx-1" type="danger" size='small'>{{ errors.title }}</el-text>
  </div>
  <div class="mt-2">
    <el-select
        v-model="category"
        class="w-100"
        placeholder="카테고리를 선택하세요"
        size="large"
    >
      <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
    <el-text v-if="errors.category" class="mx-1" type="danger" size='small'>{{ errors.category }}</el-text>
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" />
    <el-text v-if="errors.content" class="mx-1" type="danger" size='small'>{{ errors.content }}</el-text>
  </div>
  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">작성 완료</el-button>
    </div>
  </div>
</template>
<style></style>
