package com.food.delivery.filter;

import java.io.IOException;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.food.delivery.configuration.JwtManager;
import com.food.delivery.service.CustomUserDetails;
import com.food.delivery.service.CustomUserDetailsMyImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenSecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JwtManager jwtManager;
	@Autowired
	private CustomUserDetailsMyImpl customUserDetailsMyImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// get only token if the token is in the form of "Bearer token"
		
		/*
		 * if(requestTokenHeader == null) { throw new TokenException(requestTokenHeader,
		 * "Request do not contains Authorization Header");
		 * 
		 * }
		 */
		
		
		
		/*
		 * if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
		 * return headerAuth.substring(7, headerAuth.length()); }
		 */
		
		if (requestTokenHeader != null && StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
			
			//storing jwt token
			jwtToken = requestTokenHeader.substring(7, requestTokenHeader.length());
			
			
			try {
				//storing username
				username = jwtManager.getUsernameFromToken(jwtToken);
				
				
				
				System.out.println("username inside doFilterInternal() " + username);
			} catch (IllegalArgumentException e) {
				logger.info("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.info("JWT Token has expired");
			}
		} else {
			
			
			// no jwt token available or does not begin with Bearer String
			//logger.warn("JWT Token does not begin with Bearer String");
			//throw new TokenException(requestTokenHeader, "JWT Token does not begin with Bearer String");
		}

		// validate the token
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			// loading user's details by username in jwt token
			CustomUserDetails userDetails = customUserDetailsMyImpl.loadUserByUsername(username);// load User object from DB by username, which contains Password too in that row
			
			System.out.println("username inside doFilterInternal() " + userDetails.getEmail());
			System.out.println("username inside doFilterInternal() " + userDetails.getId());
			System.out.println("username inside doFilterInternal() " + userDetails.getPassword());
			System.out.println("username inside doFilterInternal() " + userDetails.getUsername());
			System.out.println("username inside doFilterInternal() " + userDetails.getAuthorities());

			// if token is valid configure Spring Security to manually set authentication
			if (Boolean.TRUE.equals(jwtManager.validateToken(jwtToken, userDetails))) {
				
				
				//EXTRA READING TASK FROM TOKEN START
				Claims allClaims = jwtManager.getAllClaimsFromToken(jwtToken);
				
				for (Entry<String, Object> entry : allClaims.entrySet()) {
		            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    	}
				//EXTRA READING TASK FROM TOKEN END

				//UsernamePasswordAuthenticationToken(Object principal, Object credentials)
				//This constructor can be safely used by any code that wishes to create a UsernamePasswordAuthenticationToken, as the AbstractAuthenticationToken.isAuthenticated() will return false.
				//UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
				//This constructor should only be used by AuthenticationManager or AuthenticationProvider implementations that are satisfied with producing a trusted (i.e.
				
				Object[] credentials = new Object[] {userDetails.getUsername(), userDetails.getEmail(), userDetails.getPassword(), userDetails.getSerialversionuid()};
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}