package com.example.common;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

  private final String TOKEN_SCHEME;
  private final AuthTokenConverter tokenConverter;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String tokenString = request.getHeader(TOKEN_SCHEME);
    if (!isEmptyString(tokenString)) {
      var userToken = tokenConverter.fromToken(tokenString);
      List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(userToken.getRole());
      var auth = new UsernamePasswordAuthenticationToken(userToken, null, auths);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }

  boolean isEmptyString(String string) {
    return string == null || string.isEmpty();
  }

}
