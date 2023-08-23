package com.bigdecimal.clasnapp.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigdecimal.clasnapp.domain.Group;
import com.bigdecimal.clasnapp.domain.User;
import com.bigdecimal.clasnapp.domain.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

  private final AdminService registrationService;

  @PostMapping("/users")
  @Operation(
    description = "POST endpoint for creating a user or list of users."
  )
  public ResponseEntity<?> createUsers(@RequestBody List<UserDto> userDtos) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(registrationService.createUsers(userDtos));
  }

  @GetMapping("/users")
  @Operation(description = "GET endpoint for fetching all the users.")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(registrationService.getAllUsers());
  }

  // @DeleteMapping("/users")
  public ResponseEntity<?> deleteUsers(@RequestBody List<User> users) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'deleteUsers'"
    );
  }

  // @PutMapping("/users/{userId}/profile")
  public ResponseEntity<?> updateUserProfile(
    @PathVariable("userId") String userId,
    @RequestBody User user
  ) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'updateUserProfile'"
    );
  }

  @PutMapping("/users/{username}")
  public ResponseEntity<?> updateUserByUsername(
    @PathVariable("username") String username,
    @RequestBody UserDto userDto
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(registrationService.updateUserByUsername(username, userDto));
  }

  // @PutMapping("/groups/{groupId}/profile")
  public ResponseEntity<?> updateGroupProfile(String groupId, Group group) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'updateGroupProfile'"
    );
  }
}
