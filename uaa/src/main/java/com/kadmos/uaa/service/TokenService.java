package com.kadmos.uaa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kadmos.uaa.domain.User;
import com.kadmos.uaa.exception.RefreshTokenException;
import com.kadmos.uaa.repo.UserRepository;
import com.kadmos.uaa.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
public class TokenService {
  final UserRepository userRepository;

  public TokenService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void getAccessToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        String username = JWTUtil.getSubject(refreshToken);

        User loadedUser;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) loadedUser = optionalUser.get();
        else throw new UsernameNotFoundException("No user found with username ".concat(username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        loadedUser
            .getPrivileges()
            .forEach(
                privilege ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName())));

        String accessToken =
            JWTUtil.createAccessToken(
                loadedUser.getUsername(),
                request.getRequestURL().toString(),
                grantedAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
      } catch (Exception exception) {
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    } else {
      throw new RefreshTokenException("Refresh token is missing");
    }
  }
}
