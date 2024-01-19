package com.food.delivery.exception;

import java.io.Serializable;

public class TokenException extends RuntimeException implements Serializable{
	
	private static final long serialVersionUID = -3329792613327080898L;
	
	public TokenException(String token, String message) {
	    super(String.format("Failed for [%s]: %s", token, message));
	  }
	
	

}
