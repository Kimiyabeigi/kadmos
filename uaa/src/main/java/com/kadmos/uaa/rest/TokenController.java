package com.kadmos.uaa.rest;

import com.kadmos.uaa.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("auth")
public class TokenController {

  final TokenService tokenService;

  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @GetMapping("refresh")
  public void getAccessToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    tokenService.getAccessToken(request, response);
  }
}
