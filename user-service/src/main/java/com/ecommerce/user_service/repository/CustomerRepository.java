package com.ecommerce.user_service.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user_service.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUsername(String username);
	Optional<Customer> findByEmail(String email);
	
	List<Customer> findCustomersCreatedBetweenTwoDates(Date date1, Date date2);
}