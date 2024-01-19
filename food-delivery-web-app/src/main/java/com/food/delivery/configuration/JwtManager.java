package com.food.delivery.configuration;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.food.delivery.service.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager {
	
	// 25 hours expiry time
		public static final long JWT_TOKEN_VALIDITY_IN_HOUR = 60;//4 * 60 * 60;

		@Autowired
		private KeystoreConfig keystoreConfig;
		
		

		@Value("${app.jwtExpirationMs}")
		private int jwtExpirationMs;

		public String generateToken(Map<String, Object> claims, String subject) {

			return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_IN_HOUR * 1000))
					.signWith(SignatureAlgorithm.RS256, keystoreConfig.getJwtSigningPrivateKey()).compact();
		}
		
		
		
		
		public String generateJwtToken(Map<String, Object> claims, Authentication authentication) {
			CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
			return Jwts.builder()
					.setClaims(claims)
					.setSubject((userPrincipal.getUsername()))
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_IN_HOUR * 1000))
					.signWith(SignatureAlgorithm.RS256, keystoreConfig.getJwtSigningPrivateKey())
					.compact();
		}
		

		// get username from token
		public String getUsernameFromToken(String token) {
			return getClaimFromToken(token, Claims::getSubject);
		}

		// get user id from token
		public String getIdFromToken(String token) {
			return getClaimFromToken(token, Claims::getId);
		}

		// get claim from token
		public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
			final Claims claims = getAllClaimsFromToken(token);
			
			System.out.println("claims JwtManager : " + claims);
			System.out.println("claims claimsResolver JwtManager: " + claimsResolver.apply(claims));
			return claimsResolver.apply(claims);
		}

		// get information from token
		public Claims getAllClaimsFromToken(String token) {
			return Jwts.parser().setSigningKey(keystoreConfig.getJwtSigningPrivateKey()).parseClaimsJws(token).getBody();
		}

		// validate token
		public Boolean validateToken(String token, UserDetails userDetails) {
			final String username = getUsernameFromToken(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		}

		// get expiration time from jwt token
		public Date getExpirationDateFromToken(String token) {
			return getClaimFromToken(token, Claims::getExpiration);
		}

		// check if token is expired
		private Boolean isTokenExpired(String token) {
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		}

}
