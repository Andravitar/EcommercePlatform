package com.ecommerce.user_service.dto;

import java.util.Date;

public record CustomerDTO (
	Long id, String username, String password, String email, Date createdAt
) {}
