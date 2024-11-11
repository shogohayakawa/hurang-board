package com.example.hurang_board.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 投稿登録フォーム
 */
@Data
public class PostRegisterForm {
	@NotBlank(message = "本文を入力してください。")
	private String content;
}
