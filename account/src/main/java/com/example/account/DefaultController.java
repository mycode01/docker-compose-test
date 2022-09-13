package com.example.account;

import com.example.account.dto.LoginRequest;
import com.example.account.dto.UserRegisterDto;
import com.example.account.service.RegisterService;
import com.example.common.JwtProvider;
import com.example.common.UserToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DefaultController {

  private final JwtProvider jwtProvider;
  private final AuthenticationManager authManager;
  private final RegisterService registerService;


  @GetMapping("hello")
  public String helloAccount() {
    return "hello-account";
  }

  @GetMapping("hello2")
  public String helloAccount2() {
    return "hello-account2";
  }

  @PostMapping("auth/join")
  public UserRegisterDto userRegister(@RequestBody UserRegisterDto dto) {
    return registerService.register(dto);
  }

  @GetMapping("me")
  public String me(@AuthenticationPrincipal UserToken userToken) {
    return userToken.getUsername();
  }

  @PostMapping("auth")
  public ResponseEntity<String> login(@RequestBody LoginRequest req) {
    try {
      var auth = authManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.getUsername()
              , req.getPassword()));
      String tokenString = jwtProvider.generate(auth);
      return ResponseEntity.ok(tokenString);

    } catch (BadCredentialsException bce) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) { // jjwt 라이브러리에 의존성을 가지고싶지 않으니 새로만들것
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("bear")
  public ResponseEntity<Animal> bear() {
    return ResponseEntity.ok(new Animal("곰", "곰과", "쮸뿌쮸뿌"));
  }

  @Data
  public static class Animal {

    private String name;
    private String classification;
    private String sound;

    public Animal(String name, String classification, String sound) {
      this.name = name;
      this.classification = classification;
      this.sound = sound;
    }
  }
}
