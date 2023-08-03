package com.kakjziblog.api.controller;

import static com.kakjziblog.api.domain.Category.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.config.KakjziMockUser;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.repository.comment.CommentRepository;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.request.comment.CommentCreate;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void clean(){
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @KakjziMockUser
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        //given
        User user = User.builder()
                        .email("jiwoo.sin@naver.com")
                        .name("신지우1")
                        .password("1234")
                        .build();
        userRepository.save(user);


        Post post = Post.builder()
                        .title("123456789012345")
                        .content("테스트입니다.")
                        .category(DEVELOP)
                        .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                                             .author("신지우")
                                             .password("123456")
                                             .content("댓글 테스트입니다. 아아아아 10글자 제한입니다.")
                                             .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(json))
               .andDo(print())
               .andExpect(status().isOk());

        //then

    }

}