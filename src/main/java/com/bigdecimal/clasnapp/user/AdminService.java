package com.bigdecimal.clasnapp.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bigdecimal.clasnapp.group.Group;
import com.bigdecimal.clasnapp.group.GroupName;
import com.bigdecimal.clasnapp.group.GroupRepository;

import lombok.RequiredArgsConstructor;

/*
 * Implements functionalities for performing admin tasks by those with 
 * required roles.
 */
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final GroupRepository groups;

    @Transactional
    public List<User> createUsers(List<User> userList) {
        //TODO Use local method to autogenerate username for each entity before persist.
        return users.saveAll(userList);
    }

    @Transactional
    public User updateUserRole(String userId, String roleName) {
        //TODO create custom exceptions to use in place NullPointerException in this case.
        Role role = roles.findByName(RoleName.valueOf(roleName)).orElseThrow(NullPointerException::new);
        User user = users.findById(UUID.fromString(userId)).orElseThrow(NullPointerException::new);
        user.setRole(role);
        return users.save(user);
    }

    @Transactional
    public User updateUserGroup(String userId, String groupName) {
        //TODO create custom exceptions to use in place NullPointerException in this case.
        Group group = groups.findByName(GroupName.valueOf(groupName)).orElseThrow(NullPointerException::new);
        User user = users.findById(UUID.fromString(userId)).orElseThrow(NullPointerException::new);
        user.setGroup(group);
        return users.save(user);
    }
}
