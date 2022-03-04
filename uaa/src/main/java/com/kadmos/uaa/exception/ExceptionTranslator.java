package com.kadmos.uaa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(RefreshTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public Error processRefreshTokenException(RefreshTokenException exception) {
    return new Error(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public Error processUsernameNotFoundException(UsernameNotFoundException exception) {
    return new Error(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
  }
}
