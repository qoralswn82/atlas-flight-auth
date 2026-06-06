package org.atlas.flight.auth.domain.auth.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.atlas.flight.core.message.ResponseCode;
import org.atlas.flight.core.message.ResponseCodeInterface;

/**
 * 로그인 실패 시 {@link org.atlas.flight.core.exception.ApiException}에 사용하는 응답 코드.
 * <p>{@code resultCode} 규칙: 로그인 식별 3글자 {@code LGN} + HTTP 상태 코드(3자리) — 의미에 맞는 HTTP 코드를 붙입니다.</p>
 */
@Getter
@AllArgsConstructor
public enum AuthLoginErrorCode implements ResponseCodeInterface {

	/** 1. 일치하는 고객 아이디가 없을 경우 — HTTP 404(Not Found)에 대응 */
	CUSTOMER_ID_NOT_FOUND("LGN404", "존재하지 않는 사용자입니다.", "입력한 고객 아이디와 일치하는 계정이 없습니다."),

	/** 2. 비밀번호가 불일치할 경우 — HTTP 401(Unauthorized)에 대응 */
	PASSWORD_MISMATCH("LGN401", "비밀번호가 일치하지 않습니다.", "입력한 비밀번호를 확인해 주세요."),

	/** 3. 그 외 로그인 처리 중 공용(원인 미분류) — HTTP 500(Internal Server Error)에 대응 */
	LOGIN_FAILED("LGN500", "로그인에 실패했습니다.", "잠시 후 다시 시도해 주세요.");

	private final String resultCode;
	private final String resultMessage;
	private final String resultDetailMessage;

	@Override
	public ResponseCode getResponseCode() {
		return ResponseCode.builder()
				.resultCode(resultCode)
				.resultMessage(resultMessage)
				.resultDetailMessage(resultDetailMessage)
				.build();
	}
}
