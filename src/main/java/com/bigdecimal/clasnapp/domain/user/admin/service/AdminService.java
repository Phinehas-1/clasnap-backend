package com.bigdecimal.clasnapp.domain.user.admin.service;

import com.bigdecimal.clasnapp.domain.calendar.Calendar;
import com.bigdecimal.clasnapp.domain.calendar.CalendarRepository;
import com.bigdecimal.clasnapp.domain.calendar.CalendarRequest;
import com.bigdecimal.clasnapp.domain.group.Group;
import com.bigdecimal.clasnapp.domain.group.GroupName;
import com.bigdecimal.clasnapp.domain.group.GroupRepository;
import com.bigdecimal.clasnapp.domain.role.Role;
import com.bigdecimal.clasnapp.domain.role.RoleName;
import com.bigdecimal.clasnapp.domain.role.RoleRepository;
import com.bigdecimal.clasnapp.domain.user.data.User;
import com.bigdecimal.clasnapp.domain.user.data.UserRepository;
import com.bigdecimal.clasnapp.domain.user.data.UserRequest;
import com.bigdecimal.clasnapp.exceptions.NotFoundException;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final CalendarRepository calendars;
  private final PasswordEncoder passwordEncoder;
  // elevatedRole is {RoleName.MD, RoleName.SUPERVISOR}
  private Boolean userHasElevatedRole = false;
  List<User> userList;

  @Transactional
  public List<User> createUsers(List<UserRequest> userDtos) {
    Long startTime = System.currentTimeMillis();
    userList = new ArrayList<User>();
    userDtos
      .stream()
      .forEach(userDto -> {
        // create user and assign role(s) to the user.
        if (userDto.roleNames() == null || userDto.roleNames().isEmpty()) {
          throw new IllegalArgumentException("User must be assigned a role.");
        }
        if (
          userDto.firstName() == null || userDto.firstName().trim().isBlank()
        ) {
          throw new IllegalArgumentException("User must have a 'first name'.");
        }
        List<Role> roleList = userDto
          .roleNames()
          .stream()
          .map(roleName -> RoleName.valueOf(roleName))
          .toList()
          .stream()
          .map(r -> {
            return roles
              .findByName(r)
              .orElseThrow(() -> {
                return new NotFoundException(
                  String.format(
                    "find role [%s] in role table failed.",
                    r.name()
                  )
                );
              });
          })
          .toList();
        User user = new User(
          userDto.firstName(),
          userDto.lastName(),
          username(userDto.firstName()),
          passwordEncoder.encode(userDto.password()),
          roleList
        );
        // assign group to the user except in the case the user has any of MD or
        // SUPERVISOR role(s).
        roleList
          .stream()
          .forEach(role -> {
            if (
              role.getName().equals(RoleName.MD) ||
              role.getName().equals(RoleName.SUPERVISOR)
            ) {
              userHasElevatedRole = true;
              return;
            }
            userHasElevatedRole = false;
          });
        if (!userHasElevatedRole) {
          if (userDto.groupName() == null) {
            throw new IllegalStateException(
              "User without role {MD, SUPERVISOR} must be assigned a group at registration."
            );
          }
          Group group = groups
            .findByName(GroupName.valueOf(userDto.groupName()))
            .orElseThrow(() -> {
              return new NotFoundException(
                String.format(
                  "find group [%s] in group table failed.",
                  userDto.groupName()
                )
              );
            });
          user.setGroup(group);
        }
        // add the newly created user to the list of users to be saved.
        userList.add(user);
      });
    userList = users.saveAll(userList);
    Long timeTaken = System.currentTimeMillis() - startTime;
    log.info(
      "Users {} created successfully in {} ms",
      userList.stream().map(user -> user.getUsername()).toList(),
      timeTaken
    );
    return userList;
  }

  @Transactional
  public List<User> getAllUsers() {
    return users.findAll();
  }

  @Transactional
  public User getUserByUsername(String username) {
    return users
      .findByUsername(username)
      .orElseThrow(() ->
        new EntityNotFoundException(
          String.format("User [%s] not found.", username)
        )
      );
  }

  // @Transactional
  // public Calendar createCalendar(CalendarDto calendarDto) {
  //   String week = calendarDto.year().concat("_"+calendarDto.term()).concat("_"+calendarDto.week());
  //   Calendar calendar = new Calendar(calendarDto.year(), calendarDto.term(), week);
  //   calendar.setUser(null);
  //   return calendars.save(calendar);
  // }

  @Transactional
  public User updateUserByUsername(String username, UserRequest userDto) {
    User user = getUserByUsername(username);
    if (user != null) {
      if (userDto.firstName() != null) {
        user.setFirstName(userDto.firstName());
      }
      if (userDto.lastName() != null) {
        user.setLastName(userDto.lastName());
      }
      if (userDto.password() != null && !userDto.password().trim().isBlank()) {
        user.setPassword(passwordEncoder.encode(userDto.password()));
      }
      if (userDto.groupName() != null) {
        Group group = groups
          .findByName(GroupName.valueOf(userDto.groupName()))
          .orElseThrow(() ->
            new EntityNotFoundException(
              String.format("Group [%s] not found.", userDto.groupName())
            )
          );
        user.setGroup(group);
      }
      if (userDto.roleNames() != null && !userDto.roleNames().isEmpty()) {
        List<Role> newRoles = userDto
          .roleNames()
          .stream()
          .map(roleName ->
            roles
              .findByName(RoleName.valueOf(roleName))
              .orElseThrow(() ->
                new EntityNotFoundException(
                  String.format("Role [%s] not found.", roleName)
                )
              )
          )
          .toList();
        List<Role> userRoles = user.getRoles();
        userRoles.clear();
        userRoles.addAll(newRoles);
        user.setRoles(userRoles);
      }
    }
    return users.save(user);
  }

  private String username(String firstname) {
    StringBuilder builder = new StringBuilder(firstname);
    return builder
      .append('_')
      .append(UUID.randomUUID().toString().substring(33))
      .toString();
  }
}
