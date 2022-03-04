package com.kadmos.savingsa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(SavingsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Error processRefreshTokenException(SavingsException exception) {
    return new Error(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
  }
}
