package com.bigdecimal.clasnapp.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdecimal.clasnapp.domain.Group;
import com.bigdecimal.clasnapp.domain.User;
import com.bigdecimal.clasnapp.domain.UserDto;

public interface Controller {
    ResponseEntity<?> createUsers(@RequestBody List<UserDto> userDtos);

    ResponseEntity<?> deleteUsers(@RequestBody List<User> users);

    ResponseEntity<?> updateUserProfile(@PathVariable String userId, @RequestBody User user);

    ResponseEntity<?> updateUserRole(@PathVariable String userId, @RequestParam String roleName);

    ResponseEntity<?> updateUserGroup(@PathVariable String userId, @RequestParam String roleName);

    ResponseEntity<?> updateGroupProfile(@PathVariable String groupId, @RequestBody Group group);

}
