package com.ecommerce.user_service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	@Lazy
	private JwtUtil jwtUtil;
	
	@Autowired
	@Lazy
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain) throws ServletException, IOException {
		
		String token = null;
		String username = null;
		final String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if(jwtUtil.isTokenValid(token, userDetails.getUsername())) {
				Claims claims = jwtUtil.extractClaim(token, Function.identity());

			    Object rolesObject = claims.get("roles");
			    List<GrantedAuthority> authorities = new ArrayList<>();

			    if (rolesObject instanceof List<?>) {
			        List<?> roles = (List<?>) rolesObject;
			        authorities = roles.stream()
			                .filter(role -> role instanceof String)
			                .map(role -> new SimpleGrantedAuthority((String) role))
			                .collect(Collectors.toList());
			    }

			    UsernamePasswordAuthenticationToken authentication =
			            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

			    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			    SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
