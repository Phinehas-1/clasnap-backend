package com.bigdecimal.clasnapp.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    public Optional<Role> findByName(RoleName name);
}
