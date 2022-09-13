package com.example.common;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthTokenConverter {

  private final JwtProvider jwt;
  private Claims claims;

  public UserToken fromToken(String t) {
    claims = jwt.claims(t);
    var n = claims.get("username", String.class);
    var r = claims.get("role", String.class);
    return new UserToken(r, n);
  }
}
