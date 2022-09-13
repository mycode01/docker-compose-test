package com.example.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class JwtProvider {

  private final long expireMills;
  private final JwtBuilder jwtBuilder;
  private final JwtParser jwtParser;

  public String generate(Authentication auth) {
    var now = new Date();
    var expiry = new Date(now.getTime() + expireMills);

    return jwtBuilder.setSubject((String) auth.getName())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .claim("username", auth.getName())
        .claim("role", auth.getAuthorities().stream().findFirst()
            .get().toString())
        .compact();
  }

  public Claims claims(String token){
    return jwtParser.parseClaimsJws(token).getBody();
  }


  public boolean validate(String token) {
    try {
      jwtParser.parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
//      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
//      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
//      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
//      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
//      log.error("JWT claims string is empty.");
    }
    return false;
  }

}
