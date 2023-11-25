package com.bigdecimal.clasnapp.domain.user.admin.endpoints;

import com.bigdecimal.clasnapp.domain.calendar.CalendarRequest;
import com.bigdecimal.clasnapp.domain.group.Group;
import com.bigdecimal.clasnapp.domain.user.admin.service.AdminService;
import com.bigdecimal.clasnapp.domain.user.data.User;
import com.bigdecimal.clasnapp.domain.user.data.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class Admin {

  private final AdminService adminService;

  @PostMapping("/users")
  @Operation(description = "Create a list of users.")
  public ResponseEntity<?> createUsers(
    @RequestBody List<UserRequest> userDtos
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(adminService.createUsers(userDtos));
  }

  @GetMapping("/users")
  @Operation(description = "Fetch a list of all users.")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(adminService.getAllUsers());
  }

  @Operation(description = "Update a user by their username.")
  @PutMapping("/users/{username}")
  public ResponseEntity<?> updateUserByUsername(
    @PathVariable("username") String username,
    @RequestBody UserRequest userDto
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(adminService.updateUserByUsername(username, userDto));
  }

  // @Operation(description = "Create a calendar for each week.")
  // @PostMapping("/calendar")
  // public ResponseEntity<?> createCalendar(
  //   @RequestBody CalendarDto calendarDto
  // ) {
  //   return ResponseEntity
  //     .status(HttpStatus.CREATED)
  //     .body(adminService.createCalendar(calendarDto));
  // }

  // @DeleteMapping("/users")
  public ResponseEntity<?> deleteUsers(@RequestBody List<User> users) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'deleteUsers'"
    );
  }

  // @PutMapping("/groups/{groupId}/profile")
  public ResponseEntity<?> updateGroupProfile(String groupId, Group group) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'updateGroupProfile'"
    );
  }
}
