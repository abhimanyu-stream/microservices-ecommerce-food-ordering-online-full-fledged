package com.food.delivery.exception;

import java.io.Serializable;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.food.delivery.dto.ErrorMessage;








@RestControllerAdvice
public class UserControllerAdvice extends RuntimeException implements Serializable {
	private static final long serialVersionUID = -226146049952474564L;

	// UsernameNotFoundException
	@ExceptionHandler(value = UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleUsernameNotFoundException(UsernameNotFoundException exception, WebRequest request) {
		return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), exception.getMessage(),
				request.getDescription(false));
	}

	// ResponseStatusException
	@ExceptionHandler(value = ResponseStatusException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorMessage handleResponseStatusException(ResponseStatusException exception, WebRequest request) {
		return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), exception.getMessage(),
				request.getDescription(false));
	}

}
