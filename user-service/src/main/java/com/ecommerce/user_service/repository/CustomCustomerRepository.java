package com.ecommerce.user_service.repository;

import java.util.Date;
import java.util.List;

import com.ecommerce.user_service.model.Customer;

public interface CustomCustomerRepository {
    List<Customer> findCustomersCreatedBetweenTwoDates(Date date1, Date date2);
}