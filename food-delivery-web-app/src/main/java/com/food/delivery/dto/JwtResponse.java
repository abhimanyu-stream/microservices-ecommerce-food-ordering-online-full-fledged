package com.food.delivery.dto;

import java.io.Serializable;
import java.util.List;

public class JwtResponse implements Serializable{
	
	//private long id;//ok
	//private String username;//ok
	//private String email;//ok
	//private String roles;
	//private String jwt;//ok
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6639986765223940797L;
	//
	private Long id;
	private String token;//its a jwt
	private String type = "Bearer";
	private String refreshToken;
	
	private String username;
	private String email;
	private List<String> roles;
	
	//
	
	public JwtResponse() {}

	

	public JwtResponse(Long id, String token, String type, String refreshToken, String username, String email, List<String> roles) {
		super();
		this.id = id;
		this.token = token;
		this.type = type;
		this.refreshToken = refreshToken;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
	

}
