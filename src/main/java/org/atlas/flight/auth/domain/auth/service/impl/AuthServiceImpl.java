package org.atlas.flight.auth.domain.auth.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.atlas.flight.auth.client.customer.CustomerFeignClient;
import org.atlas.flight.auth.client.customer.dto.CustomerCreateFeignRequest;
import org.atlas.flight.auth.client.customer.dto.CustomerFeignResponse;
import org.atlas.flight.auth.config.jwt.JwtTokenProvider;
import org.atlas.flight.auth.domain.auth.dto.request.AuthLoginRequest;
import org.atlas.flight.auth.domain.auth.dto.request.AuthSignupRequest;
import org.atlas.flight.auth.domain.auth.dto.response.AuthLoginResponse;
import org.atlas.flight.auth.domain.auth.message.AuthLoginErrorCode;
import org.atlas.flight.auth.domain.auth.service.AuthService;
import org.atlas.flight.auth.domain.user.entity.UserEntity;
import org.atlas.flight.auth.domain.user.repository.UserRepository;
import org.atlas.flight.core.ApiResponse;
import org.atlas.flight.core.exception.ApiException;
import org.atlas.flight.core.message.ResponseCodeGeneral;
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
	private final CustomerFeignClient customerFeignClient;

	@Override
	@Transactional
	public int join(AuthSignupRequest request) {
		if (userRepository.existsByUserId(request.getUserId())) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		String salt = generateSalt();
		String hashedPassword = hashPassword(request.getPassword(), salt);

		CustomerCreateFeignRequest customerReq = CustomerCreateFeignRequest.builder()
				.customerId(request.getUserId())
				.korFirstName(request.getKorFirstName())
				.korLastName(request.getKorLastName())
				.engFirstName(request.getEngFirstName())
				.engLastName(request.getEngLastName())
				.birthday(request.getBirthday())
				.genderCd(request.getGenderCd())
				.phoneCountryCd(request.getPhoneCountryCd())
				.phoneNumber(request.getPhoneNumber())
				.email(request.getEmail())
				.preferredLangCd(request.getPreferredLangCd())
				.build();

		try {
			customerFeignClient.createCustomer(customerReq);
		} catch (FeignException e) {
			throw mapFeignToApi(e);
		}

		userRepository.save(UserEntity.ofCredentials(request.getUserId(), hashedPassword, salt, request.getUserId()));
		return 1;
	}

	@Override
	@Transactional(readOnly = true)
	public AuthLoginResponse login(AuthLoginRequest request) {
		UserEntity user = userRepository.findByUserId(request.getUserId())
				.orElseThrow(() -> new ApiException(AuthLoginErrorCode.USER_ID_NOT_FOUND));

		String inputHash;
		try {
			inputHash = hashPassword(request.getPassword(), user.getSalt());
		} catch (RuntimeException ignored) {
			throw new ApiException(AuthLoginErrorCode.LOGIN_FAILED);
		}

		if (user.getPassword() == null) {
			throw new ApiException(AuthLoginErrorCode.LOGIN_FAILED);
		}

		boolean passwordMatched = MessageDigest.isEqual(
				inputHash.getBytes(StandardCharsets.UTF_8),
				user.getPassword().getBytes(StandardCharsets.UTF_8)
		);
		if (!passwordMatched) {
			throw new ApiException(AuthLoginErrorCode.PASSWORD_MISMATCH);
		}

		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), List.of("USER"));

		String displayName = resolveDisplayName(user.getUserId());

		return AuthLoginResponse.builder()
				.userId(user.getUserId())
				.userName(displayName)
				.accessToken(accessToken)
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean availableUserId(String userId) {
		return !userRepository.existsByUserId(userId);
	}

	private String resolveDisplayName(String userId) {
		try {
			ApiResponse<CustomerFeignResponse> res = customerFeignClient.getCustomer(userId);
			if (res == null || res.getData() == null) {
				return null;
			}
			CustomerFeignResponse data = res.getData();
			if (data.getKorFirstName() != null && data.getKorLastName() != null) {
				return data.getKorLastName() + data.getKorFirstName();
			}
			return userId;
		} catch (FeignException e) {
			return userId;
		}
	}

	private ApiException mapFeignToApi(FeignException e) {
		int status = e.status();
		if (status >= 400 && status < 500) {
			return new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}
		return new ApiException(ResponseCodeGeneral.UNKNOWN);
	}

	private String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	private String hashPassword(String password, String salt) {
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
