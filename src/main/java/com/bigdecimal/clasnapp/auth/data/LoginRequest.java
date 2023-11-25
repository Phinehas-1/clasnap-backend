package com.bigdecimal.clasnapp.auth.data;

import com.bigdecimal.clasnapp.exceptions.InvalidUserDetailsException;

public record LoginRequest(String username, String password) {
  public LoginRequest(String username, String password) {
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
