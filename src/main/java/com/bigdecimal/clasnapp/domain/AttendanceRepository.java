package com.bigdecimal.clasnapp.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByUser(User user);

    List<Attendance> findByStudent(Student student);
}
