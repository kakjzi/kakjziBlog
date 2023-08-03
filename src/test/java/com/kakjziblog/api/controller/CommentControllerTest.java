package com.kakjziblog.api.controller;

import static com.kakjziblog.api.domain.Category.*;
import static org.assertj.core.api.Assertions.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.domain.Comment;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.repository.comment.CommentRepository;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.request.comment.CommentCreate;
import com.kakjziblog.api.request.comment.CommentDelete;

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
    @DisplayName("댓글 작성")
    void test1() throws Exception {

        PasswordEncoder passwordEncoder = new SCryptPasswordEncoder(16, 8, 1, 32, 62);
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

        //expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(json))
               .andDo(print())
               .andExpect(status().isOk());
        assertThat(commentRepository.count()).isEqualTo(1L);

        Comment comment = commentRepository.findAll()
                                           .get(0);
        assertThat(comment.getAuthor()).isEqualTo("신지우");
        assertThat(comment.getPassword()).isNotEqualTo("123456");
        boolean isMatch = passwordEncoder.matches("123456", comment.getPassword());
        assertThat(isMatch).isTrue();
        assertThat(comment.getContent()).isEqualTo("댓글 테스트입니다. 아아아아 10글자 제한입니다.");
    }

    @Test
    @DisplayName("댓글 삭제")
    void test2() throws Exception {

        PasswordEncoder passwordEncoder = new SCryptPasswordEncoder(16, 8, 1, 32, 62);
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

        String encodedPassword = passwordEncoder.encode("123456");

        Comment comment = Comment.builder()
                                 .author("신지우")
                                 .password(encodedPassword)
                                 .content("으하하하하하하하하하하하하 10글자 제한입니다.")
                                 .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete request = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(json))
               .andDo(print())
               .andExpect(status().isOk());

    }
}