package com.example.hurang_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hurang_board.entity.VerificationToken;

/**
 * メール認証リポジトリ
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	/**
	 * 特定のトークンに合致するトークン情報を取得
	 * @param token トークン文字列
	 * @return VerificationToken トークン情報
	 */
	public VerificationToken findByToken(String token);
}
