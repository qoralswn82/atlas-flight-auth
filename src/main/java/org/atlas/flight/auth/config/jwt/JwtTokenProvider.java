package org.atlas.flight.auth.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final JwtProperties jwtProperties;
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		// HS256은 RFC 7518 기준 최소 256비트(32바이트) 키가 필요함. 짧은 문자열 secret은 SHA-256으로 32바이트로 고정한다.
		byte[] keyBytes = sha256(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	private static byte[] sha256(byte[] input) {
		try {
			return MessageDigest.getInstance("SHA-256").digest(input);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}
	
	// Access Token 생성
	public String createAccessToken(String userId, List<String> roles) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());
		
		return Jwts.builder()
				.subject(userId)
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}
	
	// Refresh Token 생성
	public String createRefreshToken(String userId) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());
		
		return Jwts.builder()
				.subject(userId)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}
	
	// 토큰에서 사용자 정보 추출
	public String getUserId(String token) {
		return parseClaims(token).getSubject();
	}
	
	// 토큰에서 권한 정보 추출
	@SuppressWarnings("unchecked")
	public List<String> getRoles(String token) {
		return parseClaims(token).get("roles", List.class);
	}
	
	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("JWT token is malformed: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT token compact is empty: {}", e.getMessage());
		}
		return false;
	}
	
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}