package com.bigdecimal.clasnapp.auth.endpoints;

import com.bigdecimal.clasnapp.auth.data.LoginRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/auth")
public class Auth {

  @PostMapping("/signin")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginDto) {
    return ResponseEntity.ok("Login success");
  }
}
