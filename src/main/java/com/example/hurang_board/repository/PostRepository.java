package com.example.hurang_board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.hurang_board.entity.Board;
import com.example.hurang_board.entity.Post;
import com.example.hurang_board.entity.User;

/**
 * 投稿リポジトリ
 */
public interface PostRepository extends JpaRepository<Post, Integer> {
	/**
	 * 特定のユーザに紐づく投稿情報を作成日時の降順で取得
	 * @param user ユーザ情報
	 * @return List<Post> 投稿情報リスト
	 */
	public List<Post> findByUserOrderByCreatedAtDesc(User user);

	/**
	 * 最も直近で作成された投稿情報を取得
	 * @return Post 投稿情報
	 */
	public Post findFirstByOrderByIdDesc();

	/**
	 * 特定の掲示板に紐づく投稿情報を作成日時の降順で取得
	 * @param board 掲示板情報
	 * @return List<Post> 投稿情報リスト
	 */
	@Query("SELECT p FROM Post p WHERE p.board = :board ORDER BY p.createdAt DESC")
	public List<Post> findByBoardIdOrderByCreatedAtDesc(Board board);

	/**
	 * 特定の掲示板に紐づく投稿を削除
	 * @param board 掲示板情報
	 */
	@Modifying
	@Query("DELETE FROM Post p WHERE p.board = :board")
	public void deleteByBoardId(Board board);
}
