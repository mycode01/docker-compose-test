package com.example.contents;

import com.example.common.UserToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  @GetMapping("artwork")
  public String artwork() {
    return "returned artworks";
  }

  @GetMapping("music")
  public String music() {
    return "returned music";
  }

  @GetMapping("me")
  public String me(@AuthenticationPrincipal UserToken userToken){
    return userToken.getUsername();
  }
}
