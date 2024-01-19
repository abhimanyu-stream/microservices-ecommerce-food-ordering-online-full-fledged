package com.food.delivery.dto;


import java.io.Serializable;

public class LogOutRequest implements Serializable {

	private static final long serialVersionUID = 7124396620476683946L;
	private Long userId;

	public Long getUserId() {
		return this.userId;
	}
}