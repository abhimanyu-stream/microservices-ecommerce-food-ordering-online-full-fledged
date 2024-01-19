package com.food.delivery.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.food.delivery.dto.ErrorMessage;



@RestControllerAdvice
public class TokenControllerAdvice {

  @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorMessage handleTokenRefreshException(TokenRefreshException exception, WebRequest request) {
    return new ErrorMessage(HttpStatus.FORBIDDEN.value(),new Date(),exception.getMessage(),request.getDescription(false));
  }
  
  //TokenException
  @ExceptionHandler(value = TokenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorMessage handleTokenException(TokenException exception, WebRequest request) {
    return new ErrorMessage(HttpStatus.FORBIDDEN.value(),new Date(),exception.getMessage(),request.getDescription(false));
  }
  
  
  
}