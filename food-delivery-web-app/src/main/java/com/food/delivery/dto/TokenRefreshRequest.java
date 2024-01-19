package com.food.delivery.dto;


import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest implements Serializable {

	private static final long serialVersionUID = 3577323907495563008L;
	@NotBlank
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
