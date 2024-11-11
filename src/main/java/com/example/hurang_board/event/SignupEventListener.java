package com.example.hurang_board.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.hurang_board.entity.User;
import com.example.hurang_board.service.VerificationTokenService;

/**
 * ユーザ登録Listener
 */
@Component
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;

	/**
	 * コンストラクタ
	 * @param verificationTokenService メール認証サービス
	 * @param mailSender JavaMailSender
	 */
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}

	/**
	 * ユーザ登録イベント実行
	 * @param signupEvent ユーザ登録イベント
	 */
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString();
		verificationTokenService.create(user, token);

		String senderAddress = "springboot.postingapp@example.com";
		String recipientAddress = user.getEmail();
		String subject = "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックしてユーザー登録を完了してください。";

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}
}
