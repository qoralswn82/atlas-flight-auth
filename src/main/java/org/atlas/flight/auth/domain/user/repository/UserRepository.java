package org.atlas.flight.auth.domain.user.repository;

import org.atlas.flight.auth.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	Optional<UserEntity> findByCustomerId(String customerId);

	boolean existsByCustomerId(String customerId);
}
