package com.example.hurang_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hurang_board.entity.Role;

/**
 * ロールリポジトリ
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
	/**
	 * 特定のロール名に合致するロール情報を取得
	 * @param name ロール名
	 * @return Role ロール情報
	 */
	public Role findByName(String name);
}
