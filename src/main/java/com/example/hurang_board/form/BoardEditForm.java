package com.example.hurang_board.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 掲示板編集フォーム
 */
@Data
@AllArgsConstructor
public class BoardEditForm {
	@NotBlank(message = "タイトルを入力してください。")
	private String title;
}
