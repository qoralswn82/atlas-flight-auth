package org.atlas.flight.auth.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.atlas.flight.auth.config.jwt.JwtTokenProvider;
import org.atlas.flight.core.exception.ApiException;
import org.atlas.flight.core.message.ResponseCodeGeneral;
import org.atlas.flight.auth.domain.auth.dto.request.AuthJoinRequest;
import org.atlas.flight.auth.domain.auth.dto.request.AuthLoginRequest;
import org.atlas.flight.auth.domain.auth.dto.response.AuthLoginResponse;
import org.atlas.flight.auth.domain.auth.service.AuthService;
import org.atlas.flight.auth.domain.user.entity.UserEntity;
import org.atlas.flight.auth.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	@Transactional
	public int join(AuthJoinRequest request) {
		String salt = generateSalt();

		String encryptedPassword = encryptPassword(request.getPassword(), salt);

		request.setPassword(encryptedPassword);
		request.setSalt(salt);
		request.setRgtrId(request.getUserId());
		request.setMdfrId(request.getUserId());

		userRepository.save(UserEntity.toEntity(request));
		return 1;
	}

	@Override
	@Transactional(readOnly = true)
	public AuthLoginResponse login(AuthLoginRequest request) {
		UserEntity user = userRepository.findByUserIdAndDelYn(request.getUserId(), "N")
				.orElseThrow(() -> new ApiException(ResponseCodeGeneral.BAD_REQUEST));

		String inputPassword = encryptPassword(request.getPassword(), user.getSalt());
		if (!inputPassword.equals(user.getPassword())) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), List.of("USER"));

		return AuthLoginResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.accessToken(accessToken)
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean availableUserId(String userId) {
		return !userRepository.existsByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean availableEmail(String email) {
		return !userRepository.existsByEmail(email);
	}

	private String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	private String encryptPassword(String password, String salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			String saltedPassword = password + salt;

			byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().encodeToString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
		}
	}
}
