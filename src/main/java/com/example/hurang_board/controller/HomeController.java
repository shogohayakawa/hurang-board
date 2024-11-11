package com.example.hurang_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホームコントローラ
 */
@Controller
public class HomeController {
	/**
	 * 掲示板一覧画面表示
	 * @return String 投稿一覧画面
	 */
	@GetMapping("/")
	public String index() {
		return "redirect:/boards";
	}
}
