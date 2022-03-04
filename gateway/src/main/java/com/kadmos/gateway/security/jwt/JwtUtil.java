package com.kadmos.gateway.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
  private static final String TOKEN_SECRET_KEY = "k@dmos";

  public String getUsernameFromToken(String token) {

    return getClaimFromToken(token, Claims::getSubject);
  }

  private Date getExpirationDateFromToken(String token) {

    return getClaimFromToken(token, Claims::getExpiration);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(TOKEN_SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
  }

  private boolean isTokenExpired(String token) {
    return getExpirationDateFromToken(token).before(new Date());
  }

  public boolean isInvalid(String token) {
    return this.isTokenExpired(token);
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);

    return claimsResolver.apply(claims);
  }
}
