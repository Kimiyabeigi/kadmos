package com.kadmos.uaa.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
  private String message;
  private String description;
}
