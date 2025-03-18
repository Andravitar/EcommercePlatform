package com.ecommerce.user_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ecommerce.user_service.model.Customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository{
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> findCustomersCreatedBetweenTwoDates(Date date1, Date date2) {
        String jpql = "SELECT c FROM Customer c WHERE c.createdAt BETWEEN :date1 AND :date2";
        TypedQuery<Customer> query = entityManager.createQuery(jpql, Customer.class);
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        return query.getResultList();
    }
}
