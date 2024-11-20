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

import com.example.hurang_board.entity.Board;
import com.example.hurang_board.service.BoardService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BoardControllerTest {
	// ベースURL
	private static final String BASE_URL = "/boards";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BoardService boardService;

	@Test
	public void 未ログインの場合は掲示板一覧画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(view().name("boards/index"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は掲示板一覧画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(view().name("boards/index"));
	}

	@Test
	public void 未ログインの場合は掲示板登録画面からログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は掲示板登録画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL + "/register"))
				.andExpect(status().isOk())
				.andExpect(view().name("boards/register"));
	}

	@Test
	@Transactional
	public void 未ログインの場合は掲示板を登録せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/create").with(csrf()).param("title", "掲示板登録テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Board board = boardService.findFirstPostByOrderByIdDesc();
		assertThat(board.getTitle()).isNotEqualTo("掲示板登録テスト");
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は掲示板登録後に掲示板一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/create").with(csrf()).param("title", "掲示板登録テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));

		Board board = boardService.findFirstPostByOrderByIdDesc();
		assertThat(board.getTitle()).isEqualTo("掲示板登録テスト");
	}

	@Test
	public void 未ログインの場合は掲示板編集画面からログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	public void ログイン済みの場合は他人の掲示板編集画面から掲示板一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は自身の掲示板編集画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL + "/1/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("boards/edit"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は自身の登録した掲示板更新後に掲示板一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update").with(csrf()).param("title", "掲示板編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isPresent();
		Board board = optionalBoard.get();
		assertThat(board.getTitle()).isEqualTo("掲示板編集テスト");
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	@Transactional
	public void ログイン済みの場合は他人の登録した掲示板を更新せずに掲示板一覧にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update").with(csrf()).param("title", "掲示板編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isPresent();
		Board board = optionalBoard.get();
		assertThat(board.getTitle()).isNotEqualTo("掲示板編集テスト");
	}

	@Test
	@Transactional
	public void 未ログインの場合は掲示板を更新せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/update").with(csrf()).param("title", "掲示板編集テスト"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isPresent();
		Board board = optionalBoard.get();
		assertThat(board.getTitle()).isNotEqualTo("掲示板編集テスト");
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	@Transactional
	public void ログイン済みの場合は自身の登録した掲示板削除後に掲示板一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isEmpty();
	}

	@Test
	@WithUserDetails("tanaka.taro@example.com")
	@Transactional
	public void ログイン済みの場合は他人の登録した掲示板を削除せずに掲示板一覧画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isPresent();
	}

	@Test
	@Transactional
	public void 未ログインの場合は掲示板を削除せずにログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(post(BASE_URL + "/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		Optional<Board> optionalBoard = boardService.findBoardById(1);
		assertThat(optionalBoard).isPresent();
	}
}
