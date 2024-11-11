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
import com.example.hurang_board.entity.User;
import com.example.hurang_board.form.BoardEditForm;
import com.example.hurang_board.form.BoardRegisterForm;
import com.example.hurang_board.security.UserDetailsImpl;
import com.example.hurang_board.service.BoardService;
import com.example.hurang_board.service.PostService;

/**
 * 掲示板コントローラ
 */
@Controller
@RequestMapping("/boards")
public class BoardController {
	private final BoardService boardService;
	private final PostService postService;

	/**
	 * コンストラクタ
	 * @param boardService 掲示板サービス
	 * @param postService 投稿サービス
	 */
	public BoardController(BoardService boardService, PostService postService) {
		this.boardService = boardService;
		this.postService = postService;
	}

	/**
	 * 掲示板一覧画面表示
	 * @param userDetailsImpl ログインユーザ情報
	 * @param model モデル
	 * @return String 掲示板一覧画面
	 */
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		List<Board> boards = boardService.findAllByOrderByCreatedAtDesc();

		model.addAttribute("boards", boards);

		return "boards/index";
	}

	/**
	 * 掲示板登録画面表示
	 * @param model モデル
	 * @return String 掲示板登録画面
	 */
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("boardRegisterForm", new BoardRegisterForm());

		return "boards/register";
	}

	/**
	 * 掲示板登録
	 * @param boardRegisterForm 掲示板登録フォーム
	 * @param bindingResult バリデーションチェック結果
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 掲示板の登録に成功した場合：掲示板一覧画面（リダイレクト）<br>バリデーションチェックでエラーが発生した場合：掲示板登録画面
	 */
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated BoardRegisterForm boardRegisterForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardRegisterForm", boardRegisterForm);
			return "boards/register";
		}

		User user = userDetailsImpl.getUser();
		boardService.createBoard(boardRegisterForm, user);
		redirectAttributes.addFlashAttribute("successMessage", "掲示板を作成しました。");

		return "redirect:/boards";
	}

	/**
	 * 掲示板編集画面表示
	 * @param boardId 掲示板ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 掲示板が編集可能な場合：掲示板編集画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）<br>掲示板の編集権限がない場合：掲示板一覧画面（リダイレクト）
	 */
	@GetMapping("/{boardId}/edit")
	public String edit(@PathVariable(name = "boardId") Integer boardId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);
		User user = userDetailsImpl.getUser();

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		if (!optionalBoard.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "他ユーザが作成した掲示板の編集はできません。");
			return "redirect:/boards";
		}

		Board board = optionalBoard.get();

		model.addAttribute("board", board);
		model.addAttribute("boardEditForm", new BoardEditForm(board.getTitle()));

		return "boards/edit";
	}

	/**
	 * 掲示板編集
	 * @param boardEditForm 掲示板編集フォーム 
	 * @param bindingResult バリデーションチェック結果
	 * @param boardId 掲示板ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 掲示板の更新に成功した場合：掲示板一覧画面（リダイレクト）<br>バリデーションチェックでエラーが発生した場合；掲示板編集画面<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）<br>掲示板の編集権限がない場合：掲示板一覧画面（リダイレクト）
	 */
	@PostMapping("/{boardId}/update")
	public String update(@ModelAttribute @Validated BoardEditForm boardEditForm,
			BindingResult bindingResult,
			@PathVariable(name = "boardId") Integer boardId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);
		User user = userDetailsImpl.getUser();

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		if (!optionalBoard.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "他ユーザが作成した掲示板の編集はできません。");
			return "redirect:/boards";
		}

		Board board = optionalBoard.get();

		if (bindingResult.hasErrors()) {
			model.addAttribute("board", board);
			model.addAttribute("boardEditForm", boardEditForm);
			return "boards/edit";
		}

		boardService.updatePost(boardEditForm, board);
		redirectAttributes.addFlashAttribute("successMessage", "掲示板を編集しました。");

		return "redirect:/boards";
	}

	/**
	 * 掲示板削除
	 * @param boardId 掲示板ID
	 * @param userDetailsImpl ログインユーザ情報
	 * @param redirectAttributes リダイレクト
	 * @param model モデル
	 * @return String 掲示板の削除に成功した場合：掲示板一覧画面（リダイレクト）<br>紐づく掲示板が削除されている場合：掲示板一覧画面（リダイレクト）<br>掲示板の削除権限がない場合：掲示板一覧画面（リダイレクト）
	 */
	@PostMapping("/{boardId}/delete")
	public String delete(@PathVariable(name = "boardId") Integer boardId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Board> optionalBoard = boardService.findBoardById(boardId);
		User user = userDetailsImpl.getUser();

		if (optionalBoard.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "掲示板が削除されています。");
			return "redirect:/boards";
		}

		if (!optionalBoard.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "他ユーザが作成した掲示板の削除はできません。");
			return "redirect:/boards";
		}

		Board board = optionalBoard.get();
		postService.deletePosts(board);
		boardService.deleteBoard(board);

		redirectAttributes.addFlashAttribute("successMessage", "掲示板を削除しました。");

		return "redirect:/boards";
	}
}
