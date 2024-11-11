package com.example.hurang_board.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.hurang_board.entity.User;

/**
 * ユーザ認証
 */
public class UserDetailsImpl implements UserDetails {
	private final User user;
	private final Collection<GrantedAuthority> authorities;

	/**
	 * コンストラクタ
	 * @param user ユーザ情報
	 * @param authorities ロールのコレクション
	 */
	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	/**
	 * ユーザ情報の取得
	 * @return User ユーザ情報
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 暗号化されたパスワードを返却
	 * @return String パスワード
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * ログイン時のメールアドレスを返却
	 * @return String メールアドレス
	 */
	@Override
	public String getUsername() {
		return user.getEmail();
	}

	/**
	 * ロールのコレクションを返却
	 * @return Collection<? extends GrantedAuthority> ロールのコレクション
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * アカウントが期限切れ出なければtrueを返却
	 * @return boolean アカウント期限切れ可否
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * ユーザがロックされていなければtrueを返却
	 * @return boolean ユーザロック可否
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * ユーザのパスワードが期限切れでなければtrueを返却
	 * @return boolean ユーザパスワード期限切れ可否
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * ユーザが有効であればtrueを返却
	 * @return boolean ユーザ有効可否
	 */
	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}
}
