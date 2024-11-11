package com.example.hurang_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hurang_board.entity.User;
import com.example.hurang_board.entity.VerificationToken;
import com.example.hurang_board.event.SignupEventPublisher;
import com.example.hurang_board.form.SignupForm;
import com.example.hurang_board.service.UserService;
import com.example.hurang_board.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 認証コントローラ
 */
@Controller
public class AuthController {
	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;

	/**
	 * コンストラクタ
	 * @param userService ユーザサービス
	 * @param signupEventPublisher ユーザ登録Publisher
	 * @param verificationTokenService 認証トークンサービス
	 */
	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher,
			VerificationTokenService verificationTokenService) {
		this.userService = userService;
		this.signupEventPublisher = signupEventPublisher;
		this.verificationTokenService = verificationTokenService;
	}

	/**
	 * ログイン画面表示
	 * @return String ログイン画面
	 */
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	/**
	 * ユーザ登録画面表示
	 * @param model モデル
	 * @return String ユーザ登録画面
	 */
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "auth/signup";
	}

	/**
	 * ユーザ登録
	 * @param signupForm ユーザ登録フォーム
	 * @param bindingResult バリデーションチェック結果
	 * @param redirectAttributes リダイレクト
	 * @param httpServletRequest HTTPリクエスト
	 * @param model モデル
	 * @return String ログイン画面（リダイレクト）
	 */
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			HttpServletRequest httpServletRequest,
			Model model) {
		// メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if (userService.isEmailRegistered(signupForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}

		// パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
		if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
			bindingResult.addError(fieldError);
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("signupForm", signupForm);

			return "auth/signup";
		}

		User createdUser = userService.createUser(signupForm);
		String requestUrl = new String(httpServletRequest.getRequestURL());
		signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
		redirectAttributes.addFlashAttribute("successMessage",
				"ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、ユーザー登録を完了してください。");

		return "redirect:/login";
	}

	/**
	 * メール認証
	 * @param token トークン文字列
	 * @param model モデル
	 * @return メール認証画面
	 */
	@GetMapping("/signup/verify")
	public String verify(@RequestParam(name = "token") String token, Model model) {
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

		if (verificationToken != null) {
			User user = verificationToken.getUser();
			userService.enableUser(user);
			String successMessage = "ユーザー登録が完了しました。ログイン画面よりログインしてください。";
			model.addAttribute("successMessage", successMessage);
		} else {
			String errorMessage = "トークンが無効です。";
			model.addAttribute("errorMessage", errorMessage);
		}

		return "auth/verify";
	}
}
