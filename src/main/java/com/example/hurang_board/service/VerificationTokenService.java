package com.example.hurang_board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hurang_board.entity.User;
import com.example.hurang_board.entity.VerificationToken;
import com.example.hurang_board.repository.VerificationTokenRepository;

/**
 * メール認証サービス
 */
@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;

	/**
	 * コンストラクタ
	 * @param verificationTokenRepository メール認証リポジトリ
	 */
	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
		this.verificationTokenRepository = verificationTokenRepository;
	}

	/**
	 * ユーザ登録
	 * @param user ユーザ情報
	 * @param token トークン文字列
	 */
	@Transactional
	public void create(User user, String token) {
		VerificationToken verificationToken = new VerificationToken();

		verificationToken.setUser(user);
		verificationToken.setToken(token);

		verificationTokenRepository.save(verificationToken);
	}

	/**
	 * 特定のトークンと合致するトークン情報を取得
	 * @param token トークン文字列
	 * @return VerificationToken トークン情報
	 */
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
}
