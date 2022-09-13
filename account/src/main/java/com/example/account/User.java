package com.example.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="member") // h2에서 user 사용불가
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long no;
  @Column(unique = true, length = 20)
  private String username;
  private String pass;
  private String role;

  protected User(){} // for jpa reflect
  public User(String username, String pass, String role) {
    this.username = username;
    this.pass = pass;
    this.role = role;
  }

  public Long no() {
    return no;
  }

  public String username() {
    return username;
  }

  public String pass() {
    return pass;
  }

  public String role() {
    return role;
  }
}
