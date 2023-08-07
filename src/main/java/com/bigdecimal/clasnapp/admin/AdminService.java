package com.bigdecimal.clasnapp.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bigdecimal.clasnapp.domain.Group;
import com.bigdecimal.clasnapp.domain.GroupName;
import com.bigdecimal.clasnapp.domain.GroupRepository;
import com.bigdecimal.clasnapp.domain.Role;
import com.bigdecimal.clasnapp.domain.RoleName;
import com.bigdecimal.clasnapp.domain.RoleRepository;
import com.bigdecimal.clasnapp.domain.User;
import com.bigdecimal.clasnapp.domain.UserDto;
import com.bigdecimal.clasnapp.domain.UserRepository;
import com.bigdecimal.clasnapp.exception.NotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Implements functionalities for performing admin tasks by those with 
 * required roles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final GroupRepository groups;
    private final PasswordEncoder passwordEncoder;
    // elevatedRole is {RoleName.MD, RoleName.SUPERVISOR}
    private Boolean userHasElevatedRole = false;
    List<User> userList = new ArrayList<User>();

    @Transactional
    public List<User> createUsers(List<UserDto> userDtos) {
        Long startTime = System.currentTimeMillis();
        userDtos.stream().forEach(userDto -> {
            // create user and assign role(s) to the user.
            List<Role> roleList = userDto.roleNames().stream().map(roleName -> RoleName.valueOf(roleName))
                    .toList().stream().map(r -> {
                        return roles.findByName(r).orElseThrow(() -> {
                            return new NotFoundException(
                                    String.format("find role [%s] in role table failed.", r.name()));
                        });
                    }).toList();
            User user = new User(userDto.firstName(), userDto.lastName(), username(userDto.firstName()),
                    passwordEncoder.encode(userDto.password()), roleList);
            // assign group to the user except in the case the user has any of MD or
            // SUPERVISOR role(s).
            roleList.stream().forEach(role -> {
                if (role.getName().equals(RoleName.MD) || role.getName().equals(RoleName.SUPERVISOR)) {
                    userHasElevatedRole = true;
                    return;
                }
                userHasElevatedRole = false;
            });
            if (!userHasElevatedRole) {
                if (userDto.groupName() == null) {
                    throw new IllegalStateException(
                            "User without role {MD, SUPERVISOR} must be assigned a group at registration.");
                }
                Group group = groups.findByName(GroupName.valueOf(userDto.groupName()))
                        .orElseThrow(() -> {
                            return new NotFoundException(
                                    String.format("find group [%s] in group table failed.", userDto.groupName()));
                        });
                user.setGroup(group);
            }
            // add the newly created user to the list of users to be saved.
            userList.add(user);
        });
        userList = users.saveAll(userList);
        Long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Users {} created successfully in {} ms", userList.stream().map(user -> user.getUsername()).toList(),
                timeTaken);
        return userList;
    }

    @Transactional
    public User updateUserRole(String userId, String roleName) {
        Role role = roles.findByName(RoleName.valueOf(roleName)).orElseThrow(EntityNotFoundException::new);
        User user = users.findById(UUID.fromString(userId)).orElseThrow(EntityNotFoundException::new);
        List<Role> roleList = user.getRoles();
        roleList.add(role);
        user.setRoles(roleList);
        return users.save(user);
    }

    @Transactional
    public User updateUserGroup(String userId, String groupName) {
        Group group = groups.findByName(GroupName.valueOf(groupName)).orElseThrow(EntityNotFoundException::new);
        User user = users.findById(UUID.fromString(userId)).orElseThrow(EntityNotFoundException::new);
        user.setGroup(group);
        return users.save(user);
    }

    private String username(String firstname) {
        StringBuilder builder = new StringBuilder(firstname);
        return builder.append('_').append(UUID.randomUUID().toString().substring(33)).toString();
    }
}
