package com.bigdecimal.clasnapp.domain;

import java.util.List;

public record UserDto(String firstName, String lastName, String password, List<String> roleNames, String groupName) {
    public UserDto(String firstName, String lastName, String password, List<String> roleNames, String groupName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roleNames = roleNames;
        this.groupName = groupName;

        if (roleNames == null || roleNames.isEmpty()) {
            throw new IllegalArgumentException("User possibly missing role(s).");
        }
    }
}