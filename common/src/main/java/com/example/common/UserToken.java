package com.example.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserToken {

  private final String role;
  private final String username;
  // more
}
