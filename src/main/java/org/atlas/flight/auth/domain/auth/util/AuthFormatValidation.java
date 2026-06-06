package org.atlas.flight.auth.domain.auth.util;

/**
 * 고객 모듈 {@code CustomerCreateRequest} 검증 규칙과 동일한 패턴을 유지합니다.
 */
public final class AuthFormatValidation {
	public static final String KOREAN_ONLY_REGEXP = "^[가-힣]+$";
	public static final String KOREAN_ONLY = "한글만 입력 가능합니다.";

	public static final String ENGLISH_ONLY_REGEXP = "^[\\p{Script=Latin}\\s\\-'.]+$";
	public static final String ENGLISH_ONLY = "영문(라틴 포함)만 입력 가능합니다.";

	public static final String PASSWORD_REGEXP =
			"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,64}$";
	public static final String PASSWORD = "비밀번호는 8자 이상이며 대/소문자, 숫자, 특수문자(!@#$%^&*()_+)를 각각 1개 이상 포함해야 합니다.";

	public static final String EMAIL_REGEXP =
			"^(?=.{1,254}$)(?=[^@]{1,64}@)(?=[^@]+@[^@]{1,189}$)[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*$";
	public static final String EMAIL = "이메일 형식이 올바르지 않습니다. (@ 1개, 전체 254자 이하, local-part 64자 이하, domain 189자 이하)";

	private AuthFormatValidation() {
	}
}
