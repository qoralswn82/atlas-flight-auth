package org.atlas.flight.auth.client.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 고객 모듈 POST /customers 요청 바디와 동일한 JSON 구조입니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCreateFeignRequest {
	private String customerId;
	private String korFirstName;
	private String korLastName;
	private String engFirstName;
	private String engLastName;
	private LocalDate birthday;
	private String genderCd;
	private String phoneCountryCd;
	private String phoneNumber;
	private String email;
	private String preferredLangCd;
}
