<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";
import { format } from "date-fns";
import { ko } from "date-fns/locale";


const posts =ref([]);
const router = useRouter();

axios.get("/api/posts?page=1&size=5").then((res) => {
  res.data.forEach((r: any) => {
    const formattedDate = format(new Date(r.lastUpdateDate), "yyyy년 MM월 dd일 HH:mm:ss", { locale: ko });
    r.lastUpdateDate = formattedDate;
    posts.value.push(r);
  });
});

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{ name: 'read', params: { postId: post.id} }">
          {{post.title}}
        </router-link>
      </div>
      <div class="content">
        <p>{{post.content}}</p>
      </div>

      <div class="d-flex sub">
        <div class="category">
          {{post.category}}
        </div>
        <div class="regDate"> {{ post.lastUpdateDate }}</div>
      </div>
    </li>
  </ul>
</template>
<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;
  li {
    margin-bottom: 2rem;

    .title{
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }
      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #7e7e7e;
    }
   &:last-child {
      margin-bottom: 0;
    }
    .sub {
      margin-top: 10px;
      font-size: 0.8rem;
      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}

</style>