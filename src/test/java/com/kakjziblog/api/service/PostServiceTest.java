package com.kakjziblog.api.service;

import static com.kakjziblog.api.domain.Category.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.exception.PostNotFound;
import com.kakjziblog.api.repository.PostRepository;
import com.kakjziblog.api.request.PostCreate;
import com.kakjziblog.api.request.PostEdit;
import com.kakjziblog.api.request.PostSearch;
import com.kakjziblog.api.response.PostResponse;

@SpringBootTest
class PostServiceTest {

	@Autowired private PostService postService;

	@Autowired private PostRepository postRepository;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
	}

	@Test
	@DisplayName("글 작성")
	void test1() {
		//given
        PostCreate postCreate = PostCreate.builder()
                                          .title("제목입니다.")
                                          .content("내용입니다.")
                                          .category(DEVELOP)
                                          .build();
		//when
		postService.write(postCreate);

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
        assertEquals(DEVELOP, post.getCategory());
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
        assertEquals(DEVELOP, list.get(0).getCategory());
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
}