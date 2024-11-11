package com.example.hurang_board.event;

import org.springframework.context.ApplicationEvent;

import com.example.hurang_board.entity.User;

import lombok.Getter;

/**
 * ユーザ登録イベント
 */
@Getter
public class SignupEvent extends ApplicationEvent {
	private User user;
	private String requestUrl;

	/**
	 * コンストラクタ
	 * @param source イベントソース
	 * @param user 認証メール送信ユーザ
	 * @param requestUrl リクエストURL
	 */
	public SignupEvent(Object source, User user, String requestUrl) {
		super(source);
		this.user = user;
		this.requestUrl = requestUrl;
	}
}
