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
import org.atlas.flight.auth.domain.auth.dto.request.AuthJoinRequest;

import java.time.LocalDateTime;

/**
 * 사용자
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
	@Column(name = "USER_ID", length = 30, nullable = false)
	private String userId;

	@Column(name = "USER_NAME", length = 30, nullable = false)
	private String userName;

	@Column(name = "EMAIL", length = 64, nullable = false)
	private String email;

	@Column(name = "SALT", nullable = false)
	private String salt;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "DEL_YN", length = 1, nullable = false)
	private String delYn;

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

	public static UserEntity toEntity(AuthJoinRequest request) {
		UserEntity entity = new UserEntity();
		entity.setUserId(request.getUserId());
		entity.setUserName(request.getUserName());
		entity.setEmail(request.getEmail());
		entity.setPassword(request.getPassword());
		entity.setSalt(request.getSalt());
		entity.setDelYn("N");
		entity.setRgtrId(request.getRgtrId());
		entity.setMdfrId(request.getMdfrId());
		return entity;
	}
}
