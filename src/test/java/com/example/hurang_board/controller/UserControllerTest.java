package com.example.hurang_board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
	// ベースURL
	private static final String BASE_URL = "/user";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void 未ログインの場合は会員用のユーザー情報画面からログイン画面にリダイレクトする() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithUserDetails("suzuki.ichiro@example.com")
	public void ログイン済みの場合は会員用のユーザー情報画面が正しく表示される() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(view().name("user/index"));
	}
}
