package org.atlas.flight.auth.client.customer;

import org.atlas.flight.auth.client.customer.dto.CustomerCreateFeignRequest;
import org.atlas.flight.auth.client.customer.dto.CustomerFeignResponse;
import org.atlas.flight.core.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customer-service", url = "${customer.service.url}")
public interface CustomerFeignClient {

	@PostMapping("/customers")
	ApiResponse<Void> createCustomer(@RequestBody CustomerCreateFeignRequest request);

	@GetMapping("/customers/{customerId}")
	ApiResponse<CustomerFeignResponse> getCustomer(@PathVariable("customerId") String customerId);
}
