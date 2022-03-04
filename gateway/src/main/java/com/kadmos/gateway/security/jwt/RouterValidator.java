package com.kadmos.gateway.security.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class RouterValidator {

  private RouterValidator() {
    throw new IllegalStateException("Utility class");
  }

  private static final List<String> openApiEndpoints =
      Arrays.asList("/auth/login", "/auth/refresh", "/actuator");

  public static final Predicate<ServerHttpRequest> isSecured =
      request ->
          openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
