package org.atlas.flight.auth.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 로그인 정보 (AuthLoginResponse)")
public class AuthLoginResponse {
	/** 고객 아이디 */
	@Schema(description = "고객 아이디")
	private String customerId;
	/** 회원 번호 */
	@Schema(description = "회원 번호")
	private String customerNumber;
	/** 한글 이름(이름) */
	@Schema(description = "한글 이름(이름)")
	private String korFirstName;
	/** 한글 이름(성) */
	@Schema(description = "한글 이름(성)")
	private String korLastName;
	/** 영문 이름(이름) */
	@Schema(description = "영문 이름(이름)")
	private String engFirstName;
	/** 영문 이름(성) */
	@Schema(description = "영문 이름(성)")
	private String engLastName;
	/** 접근 토큰 */
	@Schema(description = "접근 토큰")
	private String accessToken;
}
