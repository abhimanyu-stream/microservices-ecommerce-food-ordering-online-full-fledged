package com.food.delivery.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.food.delivery.configuration.JwtManager;
import com.food.delivery.dao.RoleRepository;
import com.food.delivery.dao.UserRepository;
import com.food.delivery.dto.JwtResponse;
import com.food.delivery.dto.LoginRequest;
import com.food.delivery.dto.MessageResponse;
import com.food.delivery.dto.SignUpRequest;
import com.food.delivery.model.RefreshToken;
import com.food.delivery.model.Role;
import com.food.delivery.model.RoleEnum;
import com.food.delivery.model.User;




@Service
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CustomUserDetailsMyImpl customUserDetailsMyImpl;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	//@Autowired
	//AuthenticationManager authenticationManager;
	
	@Override
	//signUp
	public ResponseEntity<MessageResponse> signUp(SignUpRequest signUpRequest) {
		log.info("calling signUp....");
		
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();// Create a bean for BCryptPasswordEncoder
		
		// Create new user's account
				User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),passwordEncoder.encode(signUpRequest.getPassword()));
				Set<String> strRoles = signUpRequest.getRoles();
				Set<Role> roles = new HashSet<>();
				
				if (strRoles == null) {
					// Retrieving ROLE_USER from DB
					Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				} else {
					strRoles.forEach(role -> {
						
						//Create a API for upgrade/downgrade Role and specification
						switch (role) {
						case "admin | ADMIN | Admin":
							Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(adminRole);
							break;
						case "mod | MODERATOR | Moderator":
							Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(modRole);
							break;
						default:
							Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(userRole);
						}
					});
				}
				user.setRoles(roles);// setting default role i.e. RoleEnum.ROLE_USER
				userRepository.save(user);
				return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
}

	@Override
	//login
	public ResponseEntity<JwtResponse>  login(LoginRequest loginRequest) {// LoginRequest contains username and password 
		log.info("calling login....");
		CustomUserDetails userDetails;
		try {
			
			userDetails = customUserDetailsMyImpl.loadUserByUsername(loginRequest.getUsername());// 1 approach
			//userDetails = customUserDetailsMyImpl.loadUserByUsername(loginRequest.getUsername(), loginRequest.getPassword());
			

		} catch (UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}
		// Create RefreshToken
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		String refreshTokenString = refreshToken.getToken();
		if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
			log.info("password matched");

			Map<String, Object> claims = new HashMap<>();
			claims.put("username", userDetails.getUsername());
			
			claims.put("email", userDetails.getEmail());
			List<String> authorities = Arrays.asList(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
			claims.put("roles", authorities);
			claims.put("userId", userDetails.getId());
			String subject = userDetails.getUsername();
			String jwt = jwtManager.generateToken(claims, subject);
			
		
			//JwtResponse(Long id, String token, String type, String refreshToken, String username, String email, List<String> roles)
			return ResponseEntity.ok(new JwtResponse(userDetails.getId(),jwt,"Bearer",refreshTokenString,userDetails.getUsername(),userDetails.getEmail(),authorities));
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
	}
	
	//generateTokenOnRefreshToken
	public String  generateTokenOnRefreshToken(String username) {
		
		CustomUserDetails userDetails;
		
		userDetails = customUserDetailsMyImpl.loadUserByUsername(username);
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userDetails.getUsername());
		claims.put("email", userDetails.getEmail());
		List<String> authorities = Arrays.asList(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
		claims.put("roles", authorities);
		claims.put("userId", userDetails.getId());
		String subject = userDetails.getUsername();
		String jwt = jwtManager.generateToken(claims, subject);
		
		
		return jwt;
		
	}
	

	

}
