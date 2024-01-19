package com.food.delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.delivery.dao.UserRepository;
import com.food.delivery.model.User;






@Service
public class CustomUserDetailsMyImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	//@Autowired
	//private RoleRepository roleRepository;
	
	

	@Override
	@Transactional
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1.
		User user = userRepository.findByUsername(username);
		
		
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		System.out.println("Inside CustomUserDetailsMyImpl " + customUserDetails.getAuthorities());
		System.out.println("Inside CustomUserDetailsMyImpl " + customUserDetails.getUsername());
		System.out.println("Inside CustomUserDetailsMyImpl " + customUserDetails.getPassword());
		System.out.println("Inside CustomUserDetailsMyImpl " + customUserDetails.getId());
		System.out.println("Inside CustomUserDetailsMyImpl " + customUserDetails.getEmail());
		return customUserDetails;
	}

	
	@Transactional
	public CustomUserDetails loadUserByUsername(String username, String password) {
		
		User user = userRepository.findByUsernameAndPassword(username,password);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with Username " + username + "  | Password " + password);
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}


}
