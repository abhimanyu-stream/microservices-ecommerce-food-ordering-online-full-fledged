package com.food.delivery.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.delivery.configuration.JwtManager;
import com.food.delivery.dto.JwtResponse;
import com.food.delivery.dto.LogOutRequest;
import com.food.delivery.dto.LoginRequest;
import com.food.delivery.dto.MessageResponse;
import com.food.delivery.dto.SignUpRequest;
import com.food.delivery.dto.TokenRefreshRequest;
import com.food.delivery.dto.TokenRefreshResponse;
import com.food.delivery.exception.TokenRefreshException;
import com.food.delivery.model.RefreshToken;
import com.food.delivery.model.User;
import com.food.delivery.service.RefreshTokenService;
import com.food.delivery.service.UserService;



//@SecurityRequirement(name = "Bearer Authentication")//its a class level configuration

@RestController
@RequestMapping(path = "/users")
public class UserAuthoController {

	@Autowired
	private UserService userService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtManager jwtManager;

	/**
	 * API to signUp
	 * 
	 * @param The SignUpRequest that contains email, username and password. The Role
	 *            is optional. If User do not provide Role then default Role is
	 *            assigned to user i.e. ROLE_USER. [ ROLE_MODERATOR, ROLE_ADMIN,
	 *            ROLE_USER]
	 * @return Returns successful message
	 * @see
	 */

	@PostMapping(path = "/signup", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<MessageResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
		System.out.println("inside signUp");

		return (userService.signUp(signUpRequest));
	}

	/**
	 * API to Login
	 * 
	 * @param The LoginRequest that contains username and password
	 * @return Returns the JWT token
	 * @see
	 */

	@PostMapping(path = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
		System.out.println("inside login");

		return (userService.login(loginRequest));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();
		RefreshToken refreshToken = refreshTokenService.findByRefreshToken(requestRefreshToken);
		if (refreshToken == null) {
			System.out.println(requestRefreshToken
					+ "   Refresh token is not in database! Please login again by username and password!");
			throw new TokenRefreshException(requestRefreshToken,
					"Refresh token is not in database! Please login again by username and password!");
		}

		// else// RefreshToken is found in DB , now check its validity, NOTE:- here
		// return value will come only if RefreshToken is still valid , and if non-valid
		// then there only exception thrown
		RefreshToken refreshTokenCheckedAndStillValid = refreshTokenService.verifyRefreshTokenExpiration(refreshToken);
		User user = refreshTokenCheckedAndStillValid.getUser();
		Long userId = user.getId(); // find all cliams and put into token
		String token = userService.generateTokenOnRefreshToken(user.getUsername());
		return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));

	}

	// @Deprecated
	// T getById(ID id);

	@PostMapping("/logout")
	public ResponseEntity<MessageResponse> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {

		Long userId = refreshTokenService.deleteByUserId(logOutRequest.getUserId());
		return ResponseEntity.ok(new MessageResponse("User Log out successful with PK !" + logOutRequest.getUserId()+" || Refreshtoken PK "+ userId));
	}

}
