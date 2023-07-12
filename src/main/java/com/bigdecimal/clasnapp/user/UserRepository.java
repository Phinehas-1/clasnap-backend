package com.bigdecimal.clasnapp.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigdecimal.clasnapp.group.Group;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByUsername(String username);

    public List<User> findAllByGroup(Group group);
}
