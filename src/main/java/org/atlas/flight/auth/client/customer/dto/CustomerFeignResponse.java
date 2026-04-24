package org.atlas.flight.auth.client.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * GET /customers/{customerId} 응답 {@code data} 역직렬화용 (표시 이름 등 필요 필드만).
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerFeignResponse {
	private String customerId;
	private String korFirstName;
	private String korLastName;
}
