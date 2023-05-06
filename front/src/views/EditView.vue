<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const post = ref({
  id: 0,
  title: "",
  content: "",
  category: "",
});
const errors = ref({});

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  }
});
const router = useRouter();

axios.get(`/api/posts/${props.postId}`).then((res) => {
  post.value = res.data;
});

const edit = () =>{
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({ name: 'home' });
  }).catch((err) => {
    if (err.response && err.response.data && err.response.data.validation) {
      errors.value = err.response.data.validation;
    } else {
      alert('An error occurred. Please try again.');
    }
  });;
}
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
</script>
<template>
  <div>
    <el-input v-model="post.title"/>
    <el-text v-if="errors.title" class="mx-1" type="danger" size='small'>{{ errors.title }}</el-text>
  </div>
  <div class="mt-2">
    <el-select
        v-model="post.category"
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
    <el-input v-model="post.content" type="textarea" rows="15"/>
    <el-text v-if="errors.content" class="mx-1" type="danger" size='small'>{{ errors.content }}</el-text>
  </div>
  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>
</template>
<style></style>
