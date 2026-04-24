package org.atlas.flight.auth.domain.auth.util;

/**
 * 고객 모듈 {@code CustomerCreateRequest} 검증 규칙과 동일한 패턴을 유지합니다.
 */
public final class AuthFormatValidation {
	public static final String KOREAN_ONLY_REGEXP = "^[가-힣]+$";
	public static final String KOREAN_ONLY = "한글만 입력 가능합니다.";

	public static final String ENGLISH_ONLY_REGEXP = "^[\\p{Script=Latin}\\s\\-'.]+$";
	public static final String ENGLISH_ONLY = "영문(라틴 포함)만 입력 가능합니다.";

	public static final String EMAIL_ONLY_REGEXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	public static final String EMAIL_ONLY = "이메일 주소만 입력 가능합니다.";

	private AuthFormatValidation() {
	}
}
