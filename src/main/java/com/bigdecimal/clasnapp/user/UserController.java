package com.bigdecimal.clasnapp.user;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements Controller {

    private final RegistrationService registrationService;

    @PostMapping("/")
    @Override
    public ResponseEntity<?> createUsers(@RequestBody List<User> users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.createUsers(users));
    }

    @DeleteMapping("/")
    @Override
    public ResponseEntity<?> deleteUsers(@RequestBody List<User> users) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUsers'");
    }

    @PutMapping("/user/profile/{userId}")
    @Override
    public ResponseEntity<?> updateUserProfile(@PathVariable("userId") String userId, @RequestBody User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserProfile'");
    }

    @PutMapping("/user/role/{userId}")
    @Override
    public ResponseEntity<?> updateUserRole(@PathVariable("userId") String userId,
            @RequestParam("roleName") String roleName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserRole'");
    }

    @PutMapping("/user/group/{userId}")
    @Override
    public ResponseEntity<?> updateUserGroup(@PathVariable("userId") String userId, @RequestParam("groupName") String groupName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserGroup'");
    }

}
