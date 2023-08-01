package com.kakjziblog.api.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.config.KakjziMockUser;
import com.kakjziblog.api.domain.Category;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.repository.PostRepository;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.PostCreate;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.kakjzi.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public PostRepository postRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void clean(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 단건 조회 테스트")
    void test1() throws Exception {
        Post post = Post.builder()
                        .title("123456789012345")
                        .content("bar")
                        .category(Category.LIFE)
                        .build();

        postRepository.save(post);

        mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                RequestDocumentation.parameterWithName("postId")
                                        .description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("lastUpdateDate").description("생성일")
                        )
                ));
    }
    @Test
    @KakjziMockUser
    @DisplayName("글 등록")
    void test2() throws Exception {

        PostCreate request = PostCreate.builder()
                                       .title("나는 신지우")
                                       .content("반포자이")
                                       .category(Category.LIFE)
                                       .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                    .header("authorization","jiwoo")
                    .contentType(APPLICATION_JSON)
                    .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                PayloadDocumentation.fieldWithPath("title")
                                        .description("제목").attributes(key("constraint").value("좋은 제목 입력해주세요")),
                                PayloadDocumentation.fieldWithPath("content")
                                        .description("내용").optional(),
                                PayloadDocumentation.fieldWithPath("category").description("카테고리").optional()
                        )
                ));

    }
}
