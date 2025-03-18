package com.ecommerce.user_service.exception;

public class CustomerAlreadyExistingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistingException(String message) {
		super(message);
	}
	
}