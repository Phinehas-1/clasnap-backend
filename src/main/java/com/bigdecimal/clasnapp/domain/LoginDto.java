package com.bigdecimal.clasnapp.domain;

import com.bigdecimal.clasnapp.exception.InvalidUserDetailsException;

public record LoginDto(String username, String password) {
  public LoginDto(String username, String password) {
    if (
      username == null ||
      username.toLowerCase().trim().isEmpty() ||
      password == null ||
      password.trim().isEmpty()
    ) {
      throw new InvalidUserDetailsException("Invalid username or password");
    }
    this.username = username;
    this.password = password;
  }
}
