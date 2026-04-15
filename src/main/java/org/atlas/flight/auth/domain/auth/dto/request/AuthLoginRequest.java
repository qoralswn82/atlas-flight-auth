package org.atlas.flight.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 로그인 (AuthLoginRequest)")
public class AuthLoginRequest {
	@NotBlank
	@Schema(description = "사용자 아이디")
	private String userId;
	@NotBlank
	@Schema(description = "비밀번호")
	private String password;
}
