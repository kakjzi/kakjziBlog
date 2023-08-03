package com.kakjziblog.api.controller;

import static com.kakjziblog.api.domain.Category.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.config.KakjziMockUser;
import com.kakjziblog.api.domain.Category;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.post.PostCreate;
import com.kakjziblog.api.request.post.PostEdit;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean(){
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @KakjziMockUser
    @DisplayName("/posts 요청시 Hello World 를 출력합니다")
    void test() throws Exception {
        //given
//        PostCreate requeset = new PostCreate("제목입니다.","내용입니다.");

        // 빌더패턴 사용
        PostCreate requeset = PostCreate.builder()
                                        .title("제목입니다.")
                                        .content("내용입니다.")
                                        .category(DEVELOP)
                                        .build();

        String json = objectMapper.writeValueAsString(requeset);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("title 은 필수입니다.")
    void test2() throws Exception {
        //given
        PostCreate requeset = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(requeset);


        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                       .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @KakjziMockUser
    // @WithMockUser(username = "jiwoo12@naver.com", roles = {"ADMIN"})
    @DisplayName("글 작성")
    void test3() throws Exception {
        //when
        mockMvc.perform(post("/posts").contentType(APPLICATION_JSON)
                                      .content(
                                          "{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\", \"category\": \"개발\"}"))
               .andExpect(status().isOk())
               .andDo(print());
        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll()
                                  .get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
        assertEquals(DEVELOP, post.getCategory());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {

        //응답클래스 생성 편
//  만약 클라이언트가 title을 10글자로 제한해달라고한다면..?

        //given
        Post post = Post.builder()
                        .title("123456789012345")
                        .content("bar")
                        .category(DEVELOP)
                        .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andExpect(jsonPath("$.category").value("개발"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회- 페이지네이션")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                                           .mapToObj(i -> Post.builder()
                                                              .title("지우 제목 -" + i)
                                                              .content("포르쉐타자 - " + i)
                                                              .category(DEVELOP)
                                                              .build())
                                           .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=20")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("지우 제목 -19"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                                           .mapToObj(i -> Post.builder()
                                                              .title("지우 제목 -" + i)
                                                              .content("포르쉐타자 - " + i)
                                                              .category(DEVELOP)
                                                              .build())
                                           .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=20")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("지우 제목 -19"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "jiwoo12@naver.com", roles = {"ADMIN"})
    @DisplayName("게시글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                        .title("지우")
                        .content("모닝")
                        .category(Category.DEVELOP)
                        .build();
        postRepository.save(post);

        PostEdit edit = PostEdit.builder()
                                .title("지우와 반포자이")
                                .content("모닝")
                                .category(Category.DEVELOP)
                                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @KakjziMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given
        User user = userRepository.findAll()
                                  .get(0);

        Post post = Post.builder()
                        .title("지우")
                        .content("모닝")
                        .user(user)
                        .category(Category.DEVELOP)
                        .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "jiwoo12@naver.com", roles = {"ADMIN"})
    @DisplayName("존재하지않는 게시글 조회")
    void test9() throws Exception {
        //expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "jiwoo12@naver.com", roles = {"ADMIN"})
    @DisplayName("존재하지않는 게시글 수정")
    void test10() throws Exception {
        //given
        PostEdit edit = PostEdit.builder()
                                .title("지우와 반포자이")
                                .content("모닝")
                                .category(Category.DEVELOP)
                                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "jiwoo12@naver.com", roles = {"ADMIN"})
    @DisplayName("Title에 '바보'는 제외 해야하는 정책")
    void test11() throws Exception {
        //given
        PostCreate requeset = PostCreate.builder()
                                        .title("나는 바보입니다.")
                                        .content("내용입니다.")
                                        .category(Category.DEVELOP)
                                        .build();

        String json = objectMapper.writeValueAsString(requeset);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("카테고리 NULL일 경우 오류발생")
    void test12() throws Exception {
        //given
        PostCreate requeset = PostCreate.builder()
                                        .title("나는 바보입니다.")
                                        .content("내용입니다.")
                                        .category(null)
                                        .build();

        String json = objectMapper.writeValueAsString(requeset);
        mockMvc.perform(post("/posts")
                   .contentType(APPLICATION_JSON)
                   .content(json))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
               .andExpect(jsonPath("$.validation.category").value("유효한 카테고리를 입력해주세요."))
               .andDo(print());
    }
}