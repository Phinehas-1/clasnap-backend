package com.bigdecimal.clasnapp.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    public Role findByName(RoleName name);    
}
