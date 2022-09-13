package com.example.account.config;

import com.example.account.UserRepo;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class LoginDelegator implements UserDetailsService {

  private final UserRepo repo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final var user = repo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("cannot found user"));
    final var res = new FindUserNameRes(user.role(), user.pass(), user.username());
    return new LoginUserDetail(res);
  }

  public static class FindUserNameRes {

    public FindUserNameRes(String role, String pass, String username) {
      this.role = role;
      this.pass = pass;
      this.username = username;
    }

    final private String role;
    private final String pass;
    private final String username;

    public String role() {
      return role;
    }

    public String pass() {
      return pass;
    }

    public String username() {
      return username;
    }
  }

  public static class LoginUserDetail implements UserDetails {

    private final FindUserNameRes res;

    public LoginUserDetail(FindUserNameRes res) {
      this.res = res;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return AuthorityUtils.createAuthorityList(res.role());
    }

    @Override
    public String getPassword() {
      return res.pass();
    }

    @Override
    public String getUsername() {
      return res.username();
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
