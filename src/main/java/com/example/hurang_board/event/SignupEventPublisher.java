package com.example.hurang_board.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.hurang_board.entity.User;

/**
 * ユーザ登録Publisher
 */
@Component
public class SignupEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * コンストラクタ
	 * @param applicationEventPublisher
	 */
	public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * ユーザ登録イベント発行
	 * @param user 認証メール送信ユーザ
	 * @param requestUrl リクエストURL
	 */
	public void publishSignupEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
	}
}
