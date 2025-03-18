package com.ecommerce.user_service.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.user_service.dto.CustomerDTO;
import com.ecommerce.user_service.model.Customer;


@Component
public class CustomerMapper {
	public Customer toEntity(CustomerDTO dto) {
		return new Customer(dto.id(), dto.username(), dto.password(), dto.email(), dto.createdAt());
	}
	
	public CustomerDTO toDTO(Customer customer) {
		return new CustomerDTO(customer.getId(), customer.getUsername(), customer.getPassword(), customer.getEmail(), customer.getCreatedAt());
	}
}
