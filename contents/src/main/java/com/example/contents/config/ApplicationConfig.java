package com.example.contents.config;

import com.example.common.AuthTokenConverter;
import com.example.common.JwtProvider;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Value("${application.jwt.secret}")
  private String secretKey;
  @Value("${application.jwt.expiryMills}")
  private long expireMills;

  @Bean
  public JwtBuilder jwtBuilder() {
    return Jwts.builder().signWith(SignatureAlgorithm.HS256, secretKey);
  }

  @Bean
  public JwtParser jwtParser() {
    return Jwts.parserBuilder().setSigningKey(secretKey).build();
  }

  @Bean
  public JwtProvider jwtProvider(JwtParser parser, JwtBuilder builder) {
    return new JwtProvider(expireMills, builder, parser);
  }

  @Bean
  public AuthTokenConverter authTokenConverter(JwtProvider jwtProvider) {
    return new AuthTokenConverter(jwtProvider);
  }

}
