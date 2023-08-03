package com.kakjziblog.api.service;

import static com.kakjziblog.api.domain.Category.*;
import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.exception.PostNotFound;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.post.PostCreate;
import com.kakjziblog.api.request.post.PostEdit;
import com.kakjziblog.api.request.post.PostSearch;
import com.kakjziblog.api.response.PostResponse;

@SpringBootTest
class PostServiceTest {

	@Autowired private PostService postService;

	@Autowired private PostRepository postRepository;
	@Autowired private UserRepository userRepository;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("글 작성")
	void test1() {
		//given
		var user = User.builder()
					   .name("신지우")
					   .email("jiwoo.sin@naver.com")
					   .password("1234")
					   .build();
		userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                                          .title("제목입니다.")
                                          .content("내용입니다.")
                                          .category(DEVELOP)
                                          .build();
		//when
		postService.write(user.getId(), postCreate);

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
	void test2() throws Exception {
		//given
        Post requestPost = Post.builder()
                               .title("foo")
                               .content("bar")
                               .category(DEVELOP)
                               .build();

		postRepository.save(requestPost);

		//when
		PostResponse post = postService.get(requestPost.getId());

		//then
		assertNotNull(post);
		assertEquals("foo", post.getTitle());
		assertEquals("bar", post.getContent());
        assertEquals("개발", post.getCategory());
	}

	@Test
	@DisplayName("글 1페이지 조회")
	void test3() throws Exception {
		//given
        List<Post> requestPosts = IntStream.range(0, 20)
                                           .mapToObj(i -> Post.builder()
                                                              .title("지우 제목 -" + i)
                                                              .content("포르쉐타자 - " + i)
                                                              .category(DEVELOP)
                                                              .build())
                                           .collect(Collectors.toList());
		postRepository.saveAll(requestPosts);
		PostSearch postSearch = PostSearch.builder()
										  .build();

		//when
		List<PostResponse> list = postService.getList(postSearch);

		//then
		assertEquals(10L, list.size());
		assertEquals("지우 제목 -19", list.get(0).getTitle());
        assertEquals("개발", list.get(0).getCategory());
	}

	@Test
	@DisplayName("게시글 제목 수정")
	void test4() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("포르쉐")
						.build();

		postRepository.save(post);
		PostEdit edit = PostEdit.builder()
								.title("지우와 반포자이")
								.content("포르쉐")
								.build();

		//when
		postService.edit(post.getId(), edit);

		//then
		Post changedPost = postRepository.findById(post.getId())
										 .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

		assertEquals("지우와 반포자이", changedPost.getTitle());
		assertEquals("포르쉐", changedPost.getContent());
	}

	@Test
	@DisplayName("게시글 내용 수정")
	void test5() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("모닝")
						.build();

		postRepository.save(post);
		PostEdit edit = PostEdit.builder()
								.title("지우와 반포자이")
								.content("포르쉐")
								.build();
		//when
		postService.edit(post.getId(), edit);

		//then
		Post changedPost = postRepository.findById(post.getId())
										 .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

		assertEquals("지우와 반포자이", changedPost.getTitle());
		assertEquals("포르쉐", changedPost.getContent());
	}

	@Test
	@DisplayName("게시글 삭제")
	void test6() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("모닝")
						.build();

		postRepository.save(post);
		postService.delete(post.getId());

		//then
		assertEquals(0, postRepository.count());
	}

	@Test
	@DisplayName("게시글 한 건 조회- 존재 하지않는 글")
	void test7() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("모닝")
						.build();

		postRepository.save(post);

		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.get(post.getId() + 1l);
		});
	}

	@Test
	@DisplayName("게시글 삭제 - 존재하지 않는 글")
	void test8() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("모닝")
						.build();

		postRepository.save(post);

		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.delete(post.getId() + 1l);
		});
	}

	@Test
	@DisplayName("게시글 내용 수정 - 존재하지 않는 글")
	void test10() throws Exception {
		//given
		Post post = Post.builder()
						.title("지우")
						.content("모닝")
						.build();

		postRepository.save(post);
		PostEdit edit = PostEdit.builder()
								.title("지우와 반포자이")
								.content("포르쉐")
								.build();
		//when
		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.edit(post.getId() + 1L, edit);
		});
	}
    @Test
    @DisplayName("게시글 카테고리 수정")
    void test11() throws Exception {
        //given
        Post post = Post.builder()
                        .title("지우")
                        .content("모닝")
                        .category(DEVELOP)
                        .build();

        postRepository.save(post);
        PostEdit edit = PostEdit.builder()
                                .title("지우와 반포자이")
                                .content("포르쉐")
                                .category(LIFE)
                                .build();
        //when
        postService.edit(post.getId(), edit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                                         .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("지우와 반포자이", changedPost.getTitle());
        assertEquals("포르쉐", changedPost.getContent());
        assertEquals(LIFE, changedPost.getCategory());
    }

	@Test
	@DisplayName("게시물 생성 시간")
	void test12() {
		//given
		var user = User.builder()
					   .name("신지우")
					   .email("jiwoo.sin@naver.com")
					   .password("1234")
					   .build();

		userRepository.save(user);

		LocalDateTime now = LocalDateTime.now();
		PostCreate postCreate = PostCreate.builder()
										  .title("제목입니다.")
										  .content("내용입니다.")
										  .category(DEVELOP)
										  .build();
		//when
		postService.write(user.getId(), postCreate);

		//then
		Post post = postRepository.findAll()
								  .get(0);

		System.out.println("post.getCreatedAt() = " + post.getCreatedAt()+ ", post.getUpdatedAt() = " + post.getUpdatedAt());

		assertThat(post.getCreatedAt()).isAfter(now);
		assertThat(post.getUpdatedAt()).isAfter(now);
	}

	@Test
	@DisplayName("게시물 생성시간 과 수정 시간 비교")
	void test13() throws InterruptedException {
		// Given
		Post post = Post.builder()
						.title("지우")
						.content("Morning")
						.build();

		postRepository.save(post);

		PostEdit edit = PostEdit.builder()
								.title("지우 반포")
								.content("Porsche")
								.build();

		// When
		postService.edit(post.getId(), edit);

		//then
		await().atMost(3, SECONDS)
			   .untilAsserted(() -> {
				   var changedPost = postRepository.findById(post.getId())
													 .orElseThrow(() -> new RuntimeException("Post does not exist. id=" + post.getId()));

				   assertThat(changedPost.getUpdatedAt()).isAfter(changedPost.getCreatedAt());
				   System.out.println("changedPost.getCreatedAt() = " + changedPost.getCreatedAt());
				   System.out.println("changedPost.getUpdateAt() = " + changedPost.getUpdatedAt());
			   });
	}
}