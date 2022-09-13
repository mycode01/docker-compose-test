package com.example.account.service;

import com.example.account.User;
import com.example.account.UserRepo;
import com.example.account.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Primary
@Slf4j
@RequiredArgsConstructor
@Service
public class StandardRegisterService implements RegisterService {

  private final UserRepo repo;
  private final PasswordEncoder passEncoder;

  @Override
  public UserRegisterDto register(UserRegisterDto dto) {
    log.debug(dto.toString());
    repo.findByUsername(dto.getUsername())
        .ifPresent(c -> {throw new RuntimeException("username exists already");});
    var user = new User(dto.getUsername(), passEncoder.encode(dto.getPassword()), "USER");
    repo.save(user);

    var ret = new UserRegisterDto();
    ret.setUsername(dto.getUsername());
    return ret;
  }
}
