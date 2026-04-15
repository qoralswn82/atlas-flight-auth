package org.atlas.flight.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 회원가입 (AuthJoinRequest)")
public class AuthJoinRequest {
	@NotBlank
	@Max(value = 30)
	@Schema(description = "사용자 아이디", maxLength = 30)
	private String userId;
	
	@NotBlank
	@Max(value = 30)
	@Schema(description = "사용자 이름", maxLength = 30)
	private String userName;
	
	@NotBlank
	@Max(value = 64)
	@Schema(description = "이메일", maxLength = 64, example = "test@gmail.com")
	private String email;
	
	@NotBlank
	@Max(value = 30)
	@Schema(description = "비밀번호", maxLength = 30)
	private String password;
	
	@Schema(hidden = true)
	private String salt;
	
	@Schema(hidden = true)
	private String rgtrId;
	
	@Schema(hidden = true)
	private String mdfrId;
}
