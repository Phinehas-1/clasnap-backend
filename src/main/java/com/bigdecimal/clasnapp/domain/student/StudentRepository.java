package com.bigdecimal.clasnapp.domain.student;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigdecimal.clasnapp.domain.user.data.User;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findAllByUser(User user);
}
