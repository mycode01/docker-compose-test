package com.example.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
//    res.setHeader("WWW-Authenticate", "Bearer");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonStr =
        objectMapper.writeValueAsString(DefaultRes.Response(401, "required authorized"));
    ServletOutputStream os = response.getOutputStream();
    os.print(jsonStr);
    os.flush();
    os.close();
  }
}
