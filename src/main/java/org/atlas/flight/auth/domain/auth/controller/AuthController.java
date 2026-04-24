package org.atlas.flight.auth.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.flight.core.ApiResponse;
import org.atlas.flight.auth.domain.auth.dto.request.AuthLoginRequest;
import org.atlas.flight.auth.domain.auth.dto.request.AuthSignupRequest;
import org.atlas.flight.auth.domain.auth.dto.response.AuthLoginResponse;
import org.atlas.flight.auth.domain.auth.property.AuthProperties;
import org.atlas.flight.auth.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증")
public class AuthController {
	private final AuthService authService;

	/**
	 * 회원가입
	 *
	 * @param request 요청
	 * @return 회원가입 성공 여부 (1=성공, 0=실패)
	 */
	@PostMapping("/join")
	@Operation(summary = "회원가입")
	public ApiResponse<Integer> join(@Valid @RequestBody AuthSignupRequest request) {
		return ApiResponse.success(authService.join(request));
	}

	/**
	 * 로그인
	 *
	 * @param request 요청
	 * @param response HttpServletResponse
	 * @return 로그인 정보 (성공했을 때, 사용자 정보가 있음)
	 */
	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ApiResponse<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request, HttpServletResponse response) {
		AuthLoginResponse loginInfo = authService.login(request);

		// HttpOnly 쿠키로 토큰 설정
		Cookie cookie = new Cookie(AuthProperties.accessToken, loginInfo.getAccessToken());
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);
		cookie.setAttribute("SameSite", "Strict"); // CSRF 방지

		response.addCookie(cookie);

		return ApiResponse.success(loginInfo);
	}

	/**
	 * 로그아웃
	 *
	 * @param response HttpServletResponse
	 * @return 없음
	 */
	@PostMapping("/logout")
	@Operation(summary = "로그아웃")
	public ApiResponse<Void> logout(HttpServletResponse response) {
		// 쿠키 삭제
		Cookie cookie = new Cookie(AuthProperties.accessToken, "");
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(0);

		response.addCookie(cookie);

		return ApiResponse.success();
	}

	/**
	 * 사용자 아이디 사용 가능 여부
	 *
	 * @param userId 사용자 아이디
	 * @return true = 사용 가능, false = 사용 불가
	 */
	@GetMapping("/available/user_id")
	@Operation(summary = "사용자 아이디 사용 가능 여부", description = "응답 true = 사용 가능, false = 사용 불가")
	public ApiResponse<Boolean> availableUserId(@RequestParam String userId) {
		// TODO 보안상, Rate limiting 반드시 필요
		return ApiResponse.success(authService.availableUserId(userId));
	}
}
