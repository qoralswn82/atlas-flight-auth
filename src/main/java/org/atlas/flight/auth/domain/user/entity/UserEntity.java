package org.atlas.flight.auth.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 인증 전용 사용자 (자격 증명). 프로필은 고객 모듈에서 관리합니다.
 */
@Entity
@Table(name = "TBL_USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	@Id
	@Column(name = "CUSTOMER_ID", length = 30, nullable = false)
	private String customerId;

	@Column(name = "PASSWORD", nullable = false, length = 255)
	private String password;

	@Column(name = "SALT", nullable = false, length = 128)
	private String salt;

	@Column(name = "REG_DT")
	private LocalDateTime regDt;

	@Column(name = "RGTR_ID", length = 30)
	private String rgtrId;

	@Column(name = "MDFCN_DT")
	private LocalDateTime mdfcnDt;

	@Column(name = "MDFR_ID", length = 30)
	private String mdfrId;

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		if (regDt == null) {
			regDt = now;
		}
		if (mdfcnDt == null) {
			mdfcnDt = now;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		mdfcnDt = LocalDateTime.now();
	}

	public static UserEntity ofCredentials(String customerId, String hashedPassword, String salt, String actorId) {
		return UserEntity.builder()
				.customerId(customerId)
				.password(hashedPassword)
				.salt(salt)
				.rgtrId(actorId)
				.mdfrId(actorId)
				.build();
	}
}
