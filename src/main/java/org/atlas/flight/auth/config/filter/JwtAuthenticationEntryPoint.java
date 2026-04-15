package org.atlas.flight.auth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.atlas.flight.core.ApiResponse;
import org.atlas.flight.core.message.ResponseCodeGeneral;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT 인증 실패 시 처리하는 EntryPoint
 * 인증되지 않은 요청에 대해 401 Unauthorized 응답을 반환
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String requestURI = request.getRequestURI();
		log.warn("Unauthorized access attempt to: {}", requestURI);
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		
		ApiResponse<Object> errorResponse = ApiResponse.error(ResponseCodeGeneral.UNAUTHORIZED);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		
		response.getWriter().write(jsonResponse);
	}
}