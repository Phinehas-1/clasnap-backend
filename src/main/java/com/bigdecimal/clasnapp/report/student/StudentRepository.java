package com.bigdecimal.clasnapp.report.student;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigdecimal.clasnapp.user.User;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findAllByUser(User user);
}
