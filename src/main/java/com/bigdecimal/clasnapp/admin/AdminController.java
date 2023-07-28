package com.bigdecimal.clasnapp.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class AdminController implements Controller {

    private final AdminService registrationService;

    @PostMapping("/users")
    @Operation(
        description = "POST endpoint for creating a user or list of users."
    )
    public ResponseEntity<?> createUsers(@RequestBody List<UserDto> userDtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.createUsers(userDtos));
    }

    @DeleteMapping("/users")
    @Override
    public ResponseEntity<?> deleteUsers(@RequestBody List<User> users) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUsers'");
    }

    @PutMapping("/users/{userId}/profile")
    @Override
    public ResponseEntity<?> updateUserProfile(@PathVariable("userId") String userId, @RequestBody User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserProfile'");
    }

    @PutMapping("/users/{userId}/role")
    @Override
    public ResponseEntity<?> updateUserRole(@PathVariable("userId") String userId,
            @RequestParam("roleName") String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.updateUserRole(userId, roleName));
    }

    @PutMapping("/users/{userId}/group")
    @Override
    public ResponseEntity<?> updateUserGroup(@PathVariable("userId") String userId,
            @RequestParam("groupName") String groupName) {
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.updateUserGroup(userId, groupName));
    }

    @PutMapping("/groups/{groupId}/profile")
    @Override
    public ResponseEntity<?> updateGroupProfile(String groupId, Group group) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateGroupProfile'");
    }

}
