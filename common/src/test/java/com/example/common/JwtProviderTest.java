package com.example.common;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

class JwtProviderTest {

  static class TestAuthenticate implements Authentication{

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      var l = new ArrayList<GrantedAuthority>();

      l.add(()-> "tester");
      return l;
    }

    @Override
    public Object getCredentials() {
      return null;
    }

    @Override
    public Object getDetails() {
      return null;
    }

    @Override
    public Object getPrincipal() {
      return null;
    }

    @Override
    public boolean isAuthenticated() {
      return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
      return "testerId";
    }
  }

  int countChar(String str, char c) {
    Pattern pattern = Pattern.compile("[" + c + "]");
    Matcher matcher = pattern.matcher(str);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  @Test
  void generate() {

    var jwtProvider = new JwtProvider(10000l, Jwts.builder(), Jwts.parserBuilder().build());
    var tokenString = jwtProvider.generate(new TestAuthenticate());

    assertNotNull(tokenString);
    assertFalse(tokenString.isEmpty());
    assertTrue(countChar(tokenString, '.') == 2);

  }

  @Test
  void claims() {
    Key k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    var builder = Jwts.builder().signWith(k);
    var parser = Jwts.parserBuilder().setSigningKey(k).build();
    var jwtProvider = new JwtProvider(10000l, builder, parser);
    var tokenString = jwtProvider.generate(new TestAuthenticate());
    var claims = jwtProvider.claims(tokenString);
    var name = claims.get("username");
    assertNotNull(name);
  }

  @Test
  void validate() {
    Key k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    var builder = Jwts.builder().signWith(k);
    var parser = Jwts.parserBuilder().setSigningKey(k).build();
    var jwtProvider = new JwtProvider(1000, builder, parser);
    var validToken = jwtProvider.generate(new TestAuthenticate());
    assertTrue(jwtProvider.validate(validToken));
    assertFalse(jwtProvider.validate("thisis.not.jwtstring"));

  }
}