package com.bigdecimal.clasnapp.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    Optional<Group> findByName(GroupName name);
}
