package com.example.hurang_board.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hurang_board.entity.User;
import com.example.hurang_board.security.UserDetailsImpl;

/**
 * ユーザコントローラ
 */
@Controller
@RequestMapping("/user")
public class UserController {
	/**
	 * ユーザー情報画面表示
	 * @param userDetailsImpl ログインユーザ情報
	 * @param model モデル
	 * @return String ユーザ情報画面
	 */
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userDetailsImpl.getUser();

		model.addAttribute("user", user);

		return "user/index";
	}
}
