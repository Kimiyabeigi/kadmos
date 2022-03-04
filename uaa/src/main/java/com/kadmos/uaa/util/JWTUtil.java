package com.kadmos.uaa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class JWTUtil {

  private JWTUtil() {
    throw new IllegalStateException("Utility class");
  }

  private static final String TOKEN_SECRET_KEY = "k@dmos";
  private static final long TOKEN_EXPIRE_MINUTE = 30L * 60 * 1000; // 30 minutes
  private static final long REFRESH_TOKEN_EXPIRE_MINUTE = 720L * 60 * 1000; // 12 hour
  private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET_KEY.getBytes());

  public static String getSubject(String token) {
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getSubject();
  }

  public static String createAccessToken(String subject, String issuer, List<String> privileges) {

    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_MINUTE))
        .withIssuer(issuer)
        .withClaim("privileges", privileges)
        .sign(algorithm);
  }

  public static String createRefreshToken(String subject, String issuer) {

    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_MINUTE))
        .withIssuer(issuer)
        .sign(algorithm);
  }
}
