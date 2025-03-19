package com.ecommerce.user_service.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.dto.CustomerDTO;
import com.ecommerce.user_service.exception.CustomerAlreadyExistingException;
import com.ecommerce.user_service.exception.CustomerNotFoundException;
import com.ecommerce.user_service.mapper.CustomerMapper;
import com.ecommerce.user_service.model.Customer;
import com.ecommerce.user_service.repository.CustomerRepository;

@Service
public class CustomerService implements ICustomerService{

    @Autowired
    private CustomerRepository userRepository;
    
    @Autowired
    private CustomerMapper customerMapper;

    public List<CustomerDTO> getAllUsers() {
    	List<CustomerDTO> listCustomers = userRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    	
    	if (listCustomers.isEmpty()) {
            throw new CustomerNotFoundException("No customers found");
        }
        return listCustomers;
    }

    public CustomerDTO getUserById(Long id) {
        return userRepository.findById(id)
        		.map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException("User not found with id: " + id));
    }
    
    public CustomerDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
        		.map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException("User not found with username: " + username));
    }
    
    public CustomerDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
        		.map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException("User not found with email: " + email));
    }
    
    public List<CustomerDTO> getUserCreatedBetweenTwoDates(Date date1, Date date2) {
    	List<CustomerDTO> listCustomers = userRepository.findCustomersCreatedBetweenTwoDates(date1,date2).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    	
    	if (listCustomers.isEmpty()) {
            throw new CustomerNotFoundException("No customers found");
        }
        return listCustomers;
    }

    public CustomerDTO registerUser(CustomerDTO userDTO) {
    	userRepository.findAll().stream()
	        .filter(existingUser -> existingUser.getEmail().equals(userDTO.email()))
	        .findAny()
	        .ifPresent(existingUser -> {
	            throw new CustomerAlreadyExistingException("User already exists with username: " + userDTO.username());
	        });
    	
    	
    	Customer customer = customerMapper.toEntity(userDTO);
    	customer.setCreatedAt(new Date());
    	
        return customerMapper.toDTO(userRepository.save(customer));
    }
    
    public CustomerDTO login(String email, String password) {
        CustomerDTO result = userRepository.findByEmail(email).stream()
        	.filter(existingUser -> existingUser.getPassword().equals(password))
        	.findFirst()
        	.map(customerMapper::toDTO)
        	.orElseThrow(() -> new CustomerNotFoundException("User not found with these credentials"));
        
        return result;
    }

    public CustomerDTO updateUser(Long id, CustomerDTO userDetails) {
        Customer customer = userRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("User not found with id: " + id));
        customer.setUsername(userDetails.username());
        customer.setPassword(userDetails.password());
        customer.setEmail(userDetails.email());
        
        return customerMapper.toDTO(userRepository.save(customer));
    }

    public void deleteUser(Long id) {
    	userRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("User not found with id: " + id));
    	
        userRepository.deleteById(id);
    }
}
