package com.food.delivery.dto;


import java.io.Serializable;

public class MessageResponse implements Serializable {

	private static final long serialVersionUID = -7021768621814673520L;
	private String message;

	public MessageResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
