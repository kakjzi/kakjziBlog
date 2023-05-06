<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true
  }
});

const post = ref({
  id: 0,
  title: "",
  content: "",
  category: "",
});

const router = useRouter();
const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}});
}
onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((res) => {
    post.value = res.data;
  });
});

const moveToDelete = () => {
    axios.delete(`/api/posts/${props.postId}`).then(() => {
        router.replace({name: "home"});
        ElMessage.success('삭제되었습니다.');
    });
};
</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>

      <div class="d-flex sub">
        <div class="category">{{ post.category }}</div>
        <div class="regDate">2023.02.20 23:22:00</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row
  >
  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="danger" @click="moveToDelete()">삭제</el-button>
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.content {
  font-size: 0.95rem;
  margin-top: 10px;
  color: #616161;
  line-height: 1.5;
}
.sub {
  margin-top: 10px;
  font-size: 0.78rem;
  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>