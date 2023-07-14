package com.bigdecimal.clasnapp.user;

import com.bigdecimal.clasnapp.config.exceptions.InvalidUserDetailsException;

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
