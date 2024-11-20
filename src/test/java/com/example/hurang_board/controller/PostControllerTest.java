package com.example.hurang_board.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.hurang_board.entity.Post;
import com.example.hurang_board.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {
	// ベースURL
	private static final String BASE_URL = "/boards/{boardId}/posts";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostService postService;

	@Test
	public void 未ログインの場合は投稿一覧画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("posts/index"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は投稿一覧画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("posts/index"));
	}

	@Test
	public void 未ログインの場合は投稿登録画面からログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/register", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は投稿登録画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL + "/register", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("posts/register"));
	}

	@Test
	@Transactional
	public void 未ログインの場合は投稿を登録せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/create", 1).with(csrf()).param("content", "投稿登録テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Post post = postService.findFirstPostByOrderByIdDesc();
		assertThat(post.getContent()).isNotEqualTo("投稿登録テスト");
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は投稿登録後に投稿一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/create", 1).with(csrf()).param("content", "投稿登録テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));

		Post post = postService.findFirstPostByOrderByIdDesc();
		assertThat(post.getContent()).isEqualTo("投稿登録テスト");
	}

	@Test
	public void 未ログインの場合は投稿編集画面からログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	public void ログイン済みの場合は他人の投稿編集画面から投稿一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は自身の投稿編集画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("posts/edit"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は自身の投稿更新後に投稿詳細画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update", 1).with(csrf()).param("content", "投稿編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isPresent();
		Post post = optionalPost.get();
		assertThat(post.getContent()).isEqualTo("投稿編集テスト");
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	@Transactional
	public void ログイン済みの場合は他人の投稿を更新せずに投稿一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update", 1).with(csrf()).param("content", "投稿編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isPresent();
		Post post = optionalPost.get();
		assertThat(post.getContent()).isNotEqualTo("投稿編集テスト");
	}

	@Test
	@Transactional
	public void 未ログインの場合は投稿を更新せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update", 1).with(csrf()).param("content", "投稿編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isPresent();
		Post post = optionalPost.get();
		assertThat(post.getContent()).isNotEqualTo("投稿編集テスト");
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は自身の投稿削除後に投稿一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete", 1).with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isEmpty();
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	@Transactional
	public void ログイン済みの場合は他人の投稿を削除せずに投稿一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete", 1).with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL.replace("{boardId}", "1")));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isPresent();
	}

	@Test
	@Transactional
	public void 未ログインの場合は投稿を削除せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete", 1).with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Optional<Post> optionalPost = postService.findPostById(1);
		assertThat(optionalPost).isPresent();
	}
}
