package org.atlas.flight.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.atlas.flight.auth.domain.auth.util.AuthFormatValidation;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 회원가입: 비밀번호는 인증 모듈에 저장하고, 나머지 프로필은 고객 모듈 API로 전달합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 회원가입 (AuthSignupRequest)")
public class AuthSignupRequest {
	@NotBlank
	@Size(min = 1, max = 30)
	@Schema(description = "사용자 아이디 (고객 CUSTOMER_ID와 동일)", example = "john.snow")
	private String userId;

	@NotBlank
	@Size(min = 8, max = 64)
	@Schema(description = "평문 비밀번호", minLength = 8, maxLength = 64)
	private String password;

	@NotBlank
	@Size(min = 1, max = 30)
	@Pattern(regexp = AuthFormatValidation.KOREAN_ONLY_REGEXP, message = AuthFormatValidation.KOREAN_ONLY)
	@Schema(description = "한글 이름")
	private String korFirstName;

	@NotBlank
	@Size(min = 1, max = 30)
	@Pattern(regexp = AuthFormatValidation.KOREAN_ONLY_REGEXP, message = AuthFormatValidation.KOREAN_ONLY)
	@Schema(description = "한글 성")
	private String korLastName;

	@NotBlank
	@Size(min = 1, max = 30)
	@Pattern(regexp = AuthFormatValidation.ENGLISH_ONLY_REGEXP, message = AuthFormatValidation.ENGLISH_ONLY)
	@Schema(description = "영문 이름")
	private String engFirstName;

	@NotBlank
	@Size(min = 1, max = 30)
	@Pattern(regexp = AuthFormatValidation.ENGLISH_ONLY_REGEXP, message = AuthFormatValidation.ENGLISH_ONLY)
	@Schema(description = "영문 성")
	private String engLastName;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "생년월일")
	private LocalDate birthday;

	@NotBlank
	@Size(min = 1, max = 6)
	@Schema(description = "성별 코드", example = "MALE")
	private String genderCd;

	@NotBlank
	@Size(min = 1, max = 6)
	@Schema(description = "휴대폰 국가 코드", example = "82")
	private String phoneCountryCd;

	@NotBlank
	@Size(min = 3, max = 100)
	@Schema(description = "휴대폰 번호", example = "01012345678")
	private String phoneNumber;

	@NotBlank
	@Pattern(regexp = AuthFormatValidation.EMAIL_ONLY_REGEXP, message = AuthFormatValidation.EMAIL_ONLY)
	@Schema(description = "이메일")
	private String email;

	@Size(min = 1, max = 6)
	@Schema(description = "선호 언어 코드", example = "KOR")
	private String preferredLangCd;
}
