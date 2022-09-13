package com.example.account.dto;

import lombok.Data;

@Data
public class UserRegisterDto { // 귀찮으니 그냥 하나로 씀
  private String username;
  private String password;
}
