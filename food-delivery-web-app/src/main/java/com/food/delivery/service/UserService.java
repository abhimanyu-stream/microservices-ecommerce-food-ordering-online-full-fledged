package com.food.delivery.service;

import org.springframework.http.ResponseEntity;

import com.food.delivery.dto.JwtResponse;
import com.food.delivery.dto.LoginRequest;
import com.food.delivery.dto.MessageResponse;
import com.food.delivery.dto.SignUpRequest;





public interface UserService {
	
	ResponseEntity<MessageResponse> signUp(SignUpRequest signUpDto);
	ResponseEntity<JwtResponse>  login(LoginRequest loginRequest);
	String generateTokenOnRefreshToken(String username);

}
