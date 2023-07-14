package com.bigdecimal.clasnapp.user;

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
public class AuthenticationController {

  @PostMapping("/signin")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    return ResponseEntity.ok("Login success");
  }
}
