package com.ecommerce.user_service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;

@Component
public class JwtUtil {
	private final String SECRET_KEY = "s3cr3tK3yF0rJWTs3cur1tyPurp0s3sadmin";//put in app properties
	byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
    Key key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
	    claims.put("roles", userDetails.getAuthorities().stream()
	        .map(GrantedAuthority::getAuthority)
	        .collect(Collectors.toList()));
	    
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		return claimsResolver.apply(claims);
	}
	
	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
