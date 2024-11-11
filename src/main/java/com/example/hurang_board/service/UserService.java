package com.example.hurang_board.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hurang_board.entity.Role;
import com.example.hurang_board.entity.User;
import com.example.hurang_board.form.SignupForm;
import com.example.hurang_board.repository.RoleRepository;
import com.example.hurang_board.repository.UserRepository;

/**
 * ユーザサービス
 */
@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * コンストラクタ
	 * @param userRepository ユーザリポジトリ
	 * @param roleRepository ロールリポジトリ
	 * @param passwordEncoder パスワード暗号化
	 */
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * ユーザ登録
	 * @param signupForm ユーザ登録フォーム
	 * @return User ユーザ情報
	 */
	@Transactional
	public User createUser(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");

		user.setName(signupForm.getName());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setRole(role);
		user.setEnabled(false);

		return userRepository.save(user);
	}

	/**
	 * メールアドレス存在チェック
	 * @param email メールアドレス
	 * @return boolean メールアドレス存在チェック結果
	 */
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	}

	/**
	 * パスワードとパスワード（確認用）の整合チェック
	 * @param password パスワード
	 * @param passwordConfirmation パスワード（確認用）の
	 * @return パスワードとパスワード（確認用）の整合チェック結果
	 */
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	/**
	 * ユーザの有効化
	 * @param user ユーザ情報
	 */
	@Transactional
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}
}
