package com.food.delivery.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.food.delivery.service.CustomUserDetails;







public class UserUtil {

	public static com.food.delivery.service.CustomUserDetails getCurrentLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		return userDetails;
	}

}
