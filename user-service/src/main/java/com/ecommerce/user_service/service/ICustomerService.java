package com.ecommerce.user_service.service;

import java.util.Date;
import java.util.List;

import com.ecommerce.user_service.dto.CustomerDTO;

public interface ICustomerService {

	List<CustomerDTO> getAllUsers();

	CustomerDTO getUserById(Long id);
	
	CustomerDTO getUserByUsername(String username);
	
	CustomerDTO getUserByEmail(String email);
	
	List<CustomerDTO> getUserCreatedBetweenTwoDates(Date date1, Date date2);
	
	CustomerDTO registerUser(CustomerDTO userDTO);
	
	CustomerDTO login(String email, String password);
	
	CustomerDTO updateUser(Long id, CustomerDTO userDetails);
	
	void deleteUser(Long id);
}
