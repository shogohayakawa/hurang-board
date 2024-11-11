package com.example.hurang_board.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 掲示板登録フォーム
 */
@Data
public class BoardRegisterForm {
	@NotBlank(message = "タイトルを入力してください。")
	private String title;
}
