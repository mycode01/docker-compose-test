package com.example.account.service;

import com.example.account.dto.UserRegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakeRegisterService implements RegisterService {

  @Override
  public UserRegisterDto register(UserRegisterDto dto) {
    log.debug(dto.toString());
    var res = new UserRegisterDto();
    res.setUsername(dto.getUsername());
    return res;
  }
}
