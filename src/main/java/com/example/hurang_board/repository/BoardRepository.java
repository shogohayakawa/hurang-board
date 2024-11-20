package com.example.hurang_board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hurang_board.entity.Board;

/**
 * 掲示板リポジトリ
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {
	/**
	 * 全ての掲示板情報を作成日時の降順で取得
	 * @return List<Board> 掲示板情報リスト
	 */
	public List<Board> findAllByOrderByCreatedAtDesc();

	/**
	 * 最も直近で作成された掲示板情報を取得
	 * @return Board 掲示板情報
	 */
	public Board findFirstByOrderByIdDesc();
}
