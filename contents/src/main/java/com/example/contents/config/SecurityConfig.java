package com.example.contents.config;

import com.example.common.AuthTokenConverter;
import com.example.common.CustomAuthenticationEntryPoint;
import com.example.common.TokenAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Value("${application.auth.header.scheme}")
  private String TOKEN_SCHEME;
  private final AuthTokenConverter authTokenConverter;

  public TokenAuthFilter tokenAuthFilter(AuthTokenConverter authTokenConverter){// 공통라이브러리로
    return new TokenAuthFilter(TOKEN_SCHEME, authTokenConverter);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterAfter(tokenAuthFilter(authTokenConverter), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests()
        .antMatchers("/**").authenticated();

    http.headers().defaultsDisabled().contentTypeOptions();
    http.headers().frameOptions().disable().xssProtection().block(true);

    http.httpBasic().authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()
        .formLogin().disable()
        .logout().disable()
        .cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .accessDeniedHandler(new AccessDeniedHandlerImpl());
  }


}
