package org.atlas.flight.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * 인증 API(/auth/**)는 JWT 등 보안 필터 체인을 타지 않도록 예외 처리합니다.
 * {@code permitAll}만으로는 컨텍스트 경로·매처 조합에 따라 401이 발생할 수 있어 구분합니다.
 */
@Configuration
public class PublicAuthEndpointWebSecurityCustomizer {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				"/auth/**",
				"/swagger-ui/**",
				"/swagger-ui.html",
				"/v3/api-docs/**"
		);
	}
}
