package com.bigdecimal.clasnapp.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository users;

    @Transactional
    public List<User> createUsers(List<User> userList) {
        //TODO Use local method to autogenerate username for each entity before persist.
        return users.saveAll(userList);
    }
}
