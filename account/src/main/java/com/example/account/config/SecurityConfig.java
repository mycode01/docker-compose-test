package com.example.account.config;


import com.example.account.UserRepo;
import com.example.common.AuthTokenConverter;
import com.example.common.CustomAuthenticationEntryPoint;
import com.example.common.JwtProvider;
import com.example.common.TokenAuthFilter;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Value("${application.auth.header.scheme}")
  private String TOKEN_SCHEME;
  private final UserRepo userRepo;
  private final AuthTokenConverter authTokenConverter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(loginDelegator()).passwordEncoder(passwordEncoder());
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  public LoginDelegator loginDelegator() {
    return new LoginDelegator(userRepo);
  }

  public TokenAuthFilter tokenAuthFilter(AuthTokenConverter authTokenConverter){
    return new TokenAuthFilter(TOKEN_SCHEME, authTokenConverter);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterAfter(tokenAuthFilter(authTokenConverter), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests()
        .antMatchers("/auth/**").permitAll()
        .antMatchers("/v3/**").permitAll()
        .antMatchers("/v1/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/**").authenticated();

    http.headers().defaultsDisabled().contentTypeOptions();
    http.headers().frameOptions().disable().xssProtection().block(true);

    http.httpBasic().authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()
        .formLogin().disable()
//        .loginProcessingUrl("auth/login")
//        .usernameParameter("username")
//        .passwordParameter("password")
//        .successHandler(AuthenticationSuccessHandler)
//        .failureHandler(AuthenticationFailureHandler)
        .logout().disable()
        .cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .accessDeniedHandler(new AccessDeniedHandlerImpl());
  }
}

