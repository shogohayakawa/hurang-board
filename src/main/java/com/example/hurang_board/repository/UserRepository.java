package com.example.hurang_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hurang_board.entity.User;

/**
 * ユーザリポジトリ
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	/**
	 * 特定のメールアドレスに合致するユーザ情報を取得
	 * @param email メールアドレス
	 * @return User ユーザ情報
	 */
	public User findByEmail(String email);
}
