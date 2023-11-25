package com.bigdecimal.clasnapp.domain.user.data;

import java.util.List;

public record UserRequest(
  String firstName,
  String lastName,
  String password,
  List<String> roleNames,
  String groupName
) {
  public UserRequest(
    String firstName,
    String lastName,
    String password,
    List<String> roleNames,
    String groupName
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.roleNames = roleNames;
    this.groupName = groupName;
  }
}
