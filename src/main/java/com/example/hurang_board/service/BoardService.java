package com.example.hurang_board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hurang_board.entity.Board;
import com.example.hurang_board.entity.User;
import com.example.hurang_board.form.BoardEditForm;
import com.example.hurang_board.form.BoardRegisterForm;
import com.example.hurang_board.repository.BoardRepository;

/**
 * 掲示板サービス
 */
@Service
public class BoardService {
	private final BoardRepository boardRepository;

	/**
	 * コンストラクタ
	 * @param boardRepository 掲示板リポジトリ
	 */
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	/**
	 * 全ての掲示板情報を作成日時の降順で取得
	 * @return List<Board> 掲示板情報リスト
	 */
	public List<Board> findAllByOrderByCreatedAtDesc() {
		return boardRepository.findAllByOrderByCreatedAtDesc();
	}

	/**
	 * 特定のIDに合致する掲示板情報を取得
	 * @param id 掲示板ID
	 * @return Optional<Board> 掲示板情報
	 */
	public Optional<Board> findBoardById(Integer id) {
		return boardRepository.findById(id);
	}
	
	/**
	 * 最も直近で作成された掲示板情報を取得
	 * @return Board 掲示板情報
	 */
	public Board findFirstPostByOrderByIdDesc() {
		return boardRepository.findFirstByOrderByIdDesc();
	}

	/**
	 * 掲示板登録
	 * @param boardRegisterForm 掲示板登録フォーム
	 * @param user ログインユーザ情報
	 */
	@Transactional
	public void createBoard(BoardRegisterForm boardRegisterForm, User user) {
		Board board = new Board();
		board.setTitle(boardRegisterForm.getTitle());
		board.setUser(user);

		boardRepository.save(board);
	}

	/**
	 * 掲示板編集
	 * @param boardEditForm 掲示板編集フォーム
	 * @param board 掲示板情報
	 */
	@Transactional
	public void updatePost(BoardEditForm boardEditForm, Board board) {
		board.setTitle(boardEditForm.getTitle());

		boardRepository.save(board);
	}

	/**
	 * 掲示板削除
	 * @param board 掲示板情報
	 */
	@Transactional
	public void deleteBoard(Board board) {
		boardRepository.delete(board);
	}
}
