package com.example.hurang_board.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hurang_board.entity.Board;
import com.example.hurang_board.entity.Post;
import com.example.hurang_board.entity.User;
import com.example.hurang_board.form.PostEditForm;
import com.example.hurang_board.form.PostRegisterForm;
import com.example.hurang_board.security.UserDetailsImpl;
import com.example.hurang_board.service.BoardService;
import com.example.hurang_board.service.PostService;

/**
 * 投稿コントローラ
 */
@Controller
@RequestMapping("/boards/{boardId}/posts")
public class PostController {
	private final BoardService boardService;
	private final PostService postService;

	/**
	 * コンストラクタ
	 * @param boardService 掲示板サービス
	 * @param postService 投稿サービス
	 */
	public PostController(BoardService boardService, PostService postService) {
		this.boardService = boardService;
		this.postService = postService;
	}

	/**
	 * 投稿一覧画面表示
	 * @param boardId 投稿ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 紐づく掲示板が削除されていない場合：投稿一覧画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）
	 */
	@GetMapping
	public String index(@PathVariable(name = "boardId") Integer boardId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		Board board = optionalBoard.get();
		List<Post> posts = postService.findByBoardIdOrderByCreatedAtDesc(board);

		model.addAttribute("board", board);
		model.addAttribute("posts", posts);

		return "posts/index";
	}

	/**
	 * 投稿登録画面表示
	 * @param boardId 投稿ID
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 紐づく掲示板が削除されていない場合：投稿登録画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）
	 */
	@GetMapping("/register")
	public String register(@PathVariable(name = "boardId") Integer boardId, RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		model.addAttribute("boardId", boardId);
		model.addAttribute("postRegisterForm", new PostRegisterForm());

		return "posts/register";
	}

	/**
	 * 投稿登録
	 * @param boardId 投稿ID
	 * @param postRegisterForm 投稿登録フォーム
	 * @param bindingResult バリデーションチェック結果
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 投稿の登録に成功した場合：投稿一覧画面（リダイレクト）<br>バリデーションチェックでエラーが発生した場合；投稿登録画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）
	 */
	@PostMapping("/create")
	public String create(@PathVariable(name = "boardId") Integer boardId,
			@ModelAttribute @Validated PostRegisterForm postRegisterForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("boardId", boardId);
			model.addAttribute("postRegisterForm", postRegisterForm);
			return "posts/register";
		}

		Board board = optionalBoard.get();
		User user = userDetailsImpl.getUser();
		postService.createPost(postRegisterForm, board, user);
		redirectAttributes.addFlashAttribute("successMessage", "投稿が完了しました。");

		return "redirect:/boards/" + boardId + "/posts";
	}

	/**
	 * 投稿編集画面表示
	 * @param boardId 掲示板ID
	 * @param postId 投稿ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 投稿が編集可能な場合：投稿編集画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）
	 */
	@GetMapping("/{postId}/edit")
	public String edit(@PathVariable(name = "boardId") Integer boardId, @PathVariable(name = "postId") Integer postId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		Optional<Post> optionalPost = postService.findPostById(postId);
		Post post = optionalPost.get();

		model.addAttribute("boardId", boardId);
		model.addAttribute("postId", postId);
		model.addAttribute("postEditForm", new PostEditForm(post.getContent()));

		return "posts/edit";
	}

	/** 投稿編集
	 * @param postEditForm 投稿編集フォーム
	 * @param bindingResult バリデーションチェック結果
	 * @param boardId 掲示板ID
	 * @param postId 投稿ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 投稿の更新に成功した場合：投稿一覧画面（リダイレクト）<br>バリデーションチェックでエラーが発生した場合；投稿編集画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）<br>投稿の編集権限がない場合：投稿一覧画面（リダイレクト）
	 */
	@PostMapping("/{postId}/update")
	public String update(@ModelAttribute @Validated PostEditForm postEditForm,
			BindingResult bindingResult,
			@PathVariable(name = "boardId") Integer boardId, @PathVariable(name = "postId") Integer postId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		Optional<Post> optionalPost = postService.findPostById(postId);
		User user = userDetailsImpl.getUser();

		if (!optionalPost.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "他ユーザが作成した投稿の編集はできません。");
			return "redirect:/boards/" + boardId + "/posts";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("boardId", boardId);
			model.addAttribute("postId", postId);
			model.addAttribute("postEditForm", postEditForm);
			return "posts/edit";
		}

		Post post = optionalPost.get();
		postService.updatePost(postEditForm, post);
		redirectAttributes.addFlashAttribute("successMessage", "投稿を編集しました。");

		return "redirect:/boards/" + boardId + "/posts";
	}

	/**
	 * 投稿削除
	 * @param boardId 掲示板ID
	 * @param postId 投稿ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 投稿の削除に成功した場合：投稿一覧画面（リダイレクト）<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）<br>投稿の削除権限がない場合：投稿一覧画面（リダイレクト）
	 */
	@PostMapping("/{postId}/delete")
	public String delete(@PathVariable(name = "boardId") Integer boardId, @PathVariable(name = "postId") Integer postId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		Optional<Post> optionalPost = postService.findPostById(postId);
		User user = userDetailsImpl.getUser();

		if (!optionalPost.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "他ユーザが作成した投稿の削除はできません。");
			return "redirect:/boards/" + boardId + "/posts";
		}

		Post post = optionalPost.get();
		postService.deletePost(post);
		redirectAttributes.addFlashAttribute("successMessage", "投稿を削除しました。");

		return "redirect:/boards/" + boardId + "/posts";
	}
}
