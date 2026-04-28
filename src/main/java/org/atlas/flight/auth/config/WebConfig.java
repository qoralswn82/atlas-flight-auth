package org.atlas.flight.auth.config;

import lombok.RequiredArgsConstructor;
import org.atlas.flight.auth.config.resolver.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private final CurrentUserArgumentResolver currentUserArgumentResolver;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentUserArgumentResolver);
	}

	/**
	 * 보안 필터 체인을 건너뛰는 경로({@code web.ignoring})에도 동일한 CORS가 적용되도록 MVC 레벨에서 설정합니다.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}