package com.bigdecimal.clasnapp.user;

import java.util.List;

public record UserDto(String firstName, String lastName, List<String> roleNames) {
    
}
