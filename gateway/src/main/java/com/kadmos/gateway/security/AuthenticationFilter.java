package com.kadmos.gateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kadmos.gateway.dto.ErrorDTO;
import com.kadmos.gateway.security.jwt.JwtUtil;
import com.kadmos.gateway.security.jwt.RouterValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Slf4j
public class AuthenticationFilter implements Ordered, GlobalFilter {

  private static final String AUTHORIZATION = "Authorization";
  private final JwtUtil jwtUtil;

  public AuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    if (RouterValidator.isSecured.test(request)) {
      if (isAuthMissing(request))
        return onError(exchange, "Authorization header is missing in request");

      final String requestTokenHeader = request.getHeaders().getFirst(AUTHORIZATION);

      assert requestTokenHeader != null;
      StringTokenizer stringTokenizer = new StringTokenizer(requestTokenHeader);
      if (!stringTokenizer.hasMoreTokens())
        return onError(exchange, "The value of JWT Token does not have valid format");

      String bearer = stringTokenizer.nextToken();
      if (!"Bearer".equalsIgnoreCase(bearer))
        return onError(exchange, "The value of JWT Token does not begin with Bearer String");

      final String token = stringTokenizer.nextToken();
      try {
        if (jwtUtil.isInvalid(token)) return onError(exchange, "Authorization header is invalid");
      } catch (IllegalArgumentException e) {
        return onError(exchange, "Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        return onError(exchange, "JWT Token has expired");
      } catch (Exception e) {
        return onError(exchange, "Your JWT token is not a valid token");
      }

      if (isNotPermission(request, token))
        return onError(exchange, "You do not have permission to access this microservice");
    }
    return chain.filter(exchange);
  }

  private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    try {
      ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.toString(), errorMessage);
      ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String errorJson = objectWriter.writeValueAsString(errorDTO);
      byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);
      DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
      return exchange.getResponse().writeWith(Flux.just(buffer));
    } catch (JsonProcessingException e) {
      log.error("There is an error for making error response in filter", e);
      e.printStackTrace();
    }

    return response.setComplete();
  }

  private boolean isAuthMissing(ServerHttpRequest request) {
    return !request.getHeaders().containsKey(AUTHORIZATION);
  }

  private boolean isNotPermission(ServerHttpRequest request, String token) {
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    @SuppressWarnings("unchecked")
    List<String> privileges = (List<String>) claims.get("privileges");
    return privileges.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 200;
  }
}
