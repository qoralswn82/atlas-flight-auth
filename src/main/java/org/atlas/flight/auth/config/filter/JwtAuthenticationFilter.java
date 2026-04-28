package org.atlas.flight.auth.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.flight.auth.config.jwt.JwtTokenProvider;
import org.atlas.flight.auth.domain.auth.property.AuthProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		log.debug("JWT Authentication Filter - Processing request: {}", requestURI);
		
		String token = resolveToken(request);
		
		if (token != null && jwtTokenProvider.validateToken(token)) {
			try {
				String customerId = jwtTokenProvider.getCustomerId(token);
				List<String> roles = jwtTokenProvider.getRoles(token);
				
				log.debug("JWT Authentication successful for customer: {}", customerId);
				
				// Spring Security 인증 객체 생성
				List<GrantedAuthority> authorities = roles.stream()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
				
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(customerId, null, authorities);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				log.error("JWT Authentication failed: {}", e.getMessage());
				SecurityContextHolder.clearContext();
			}
		} else if (token == null) {
			log.debug("No JWT token found in request: {}", requestURI);
		} else {
			log.warn("Invalid JWT token for request: {}", requestURI);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
		// HttpOnly 쿠키에서 토큰 확인 (우선순위)
		if (request.getCookies() != null) {
			for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
				if (AuthProperties.accessToken.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		
		// Authorization 헤더에서 토큰 확인 (하위 호환성)
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
}