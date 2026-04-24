package org.atlas.flight.auth.domain.auth.service;

import org.atlas.flight.auth.domain.auth.dto.request.AuthLoginRequest;
import org.atlas.flight.auth.domain.auth.dto.request.AuthSignupRequest;
import org.atlas.flight.auth.domain.auth.dto.response.AuthLoginResponse;

public interface AuthService {
	int join(AuthSignupRequest request);

	AuthLoginResponse login(AuthLoginRequest request);

	boolean availableUserId(String userId);
}
