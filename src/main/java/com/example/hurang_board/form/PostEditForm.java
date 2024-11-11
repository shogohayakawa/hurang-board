package com.example.hurang_board.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 投稿編集フォーム
 */
@Data
@AllArgsConstructor
public class PostEditForm {
	@NotBlank(message = "本文を入力してください。")
	private String content;
}
