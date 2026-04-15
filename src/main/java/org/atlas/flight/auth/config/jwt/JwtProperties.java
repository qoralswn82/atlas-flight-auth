package org.atlas.flight.auth.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	/** HMAC 키 재료(임의 길이 문자열). JwtTokenProvider에서 SHA-256으로 32바이트 키로 변환한다. */
	private String secret;
	/** accessToken 유효 시간 (밀리초) => 24시간 */
	private long accessTokenExpiration;
	/** refreshToken 유효 시간 (밀리초) => 7일 */
	private long refreshTokenExpiration;
}