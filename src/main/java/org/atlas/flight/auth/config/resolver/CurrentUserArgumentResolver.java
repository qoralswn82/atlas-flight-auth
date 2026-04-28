package org.atlas.flight.auth.config.resolver;

import org.atlas.flight.auth.config.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * "@CurrentUser" 어노테이션이 붙은 파라미터에 현재 고객 아이디를 주입하는 리졸버
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class) &&
				parameter.getParameterType().equals(String.class);
	}
	
	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("사용자가 인증되지 않았습니다.");
		}
		
		return authentication.getName(); // JWT의 subject (customerId)
	}
}