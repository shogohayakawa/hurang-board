package com.example.hurang_board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hurang_board.entity.Board;
import com.example.hurang_board.entity.Post;
import com.example.hurang_board.entity.User;
import com.example.hurang_board.form.PostEditForm;
import com.example.hurang_board.form.PostRegisterForm;
import com.example.hurang_board.repository.PostRepository;

/**
 * 投稿サービス
 */
@Service
public class PostService {
	private final PostRepository postRepository;

	/**
	 * コンストラクタ
	 * @param postRepository
	 */
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	/**
	 * 特定のユーザーに紐づく投稿情報を作成日時の降順で取得
	 * @param user ログインユーザ情報
	 * @return List<Post> 投稿情報リスト
	 */
	public List<Post> findPostsByUserOrderedByCreatedAtDesc(User user) {
		return postRepository.findByUserOrderByCreatedAtDesc(user);
	}

	/**
	 * 特定のIDに合致する投稿情報を取得
	 * @param id 投稿ID
	 * @return Optional<Post> 投稿情報
	 */
	public Optional<Post> findPostById(Integer id) {
		return postRepository.findById(id);
	}

	/**
	 * 最も直近で作成された投稿情報を取得
	 * @return Post 投稿情報
	 */
	public Post findFirstPostByOrderByIdDesc() {
		return postRepository.findFirstByOrderByIdDesc();
	}

	/**
	 * 特定の掲示板に紐づく投稿情報を作成日時の降順で取得
	 * @return Post 投稿情報
	 */
	public List<Post> findByBoardIdOrderByCreatedAtDesc(Board board) {
		return postRepository.findByBoardIdOrderByCreatedAtDesc(board);
	}

	/**
	 * 投稿登録
	 * @param postRegisterForm 投稿登録フォーム
	 * @param user ログインユーザ情報
	 */
	@Transactional
	public void createPost(PostRegisterForm postRegisterForm, Board board, User user) {
		Post post = new Post();

		post.setBoard(board);
		post.setUser(user);
		post.setContent(postRegisterForm.getContent());

		postRepository.save(post);
	}

	/**
	 * 投稿編集
	 * @param postEditForm 投稿編集フォーム
	 * @param post 投稿情報
	 */
	@Transactional
	public void updatePost(PostEditForm postEditForm, Post post) {
		post.setContent(postEditForm.getContent());

		postRepository.save(post);
	}

	/**
	 * 投稿削除
	 * @param post 投稿情報
	 */
	@Transactional
	public void deletePost(Post post) {
		postRepository.delete(post);
	}

	/**
	 * 特定の掲示板に紐づく投稿を削除
	 * @param board 掲示板情報
	 */
	@Transactional
	public void deletePosts(Board board) {
		postRepository.deleteByBoardId(board);
	}
}
