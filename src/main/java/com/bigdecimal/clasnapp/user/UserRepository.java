package com.bigdecimal.clasnapp.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    public List<User> findAllByRole(Role role);
}
