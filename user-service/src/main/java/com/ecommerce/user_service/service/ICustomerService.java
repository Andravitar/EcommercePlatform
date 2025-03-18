package com.ecommerce.user_service.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.user_service.dto.CustomerDTO;

public interface ICustomerService {

	List<CustomerDTO> getAllUsers();

    Optional<CustomerDTO> getUserById(Long id);

    CustomerDTO createUser(CustomerDTO user);

    CustomerDTO updateUser(Long id, CustomerDTO userDetails);

    void deleteUser(Long id);
}
