package com.bigdecimal.clasnapp.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findAllByUser(User user);
}
