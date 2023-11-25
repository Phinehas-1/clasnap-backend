package com.bigdecimal.clasnapp.domain.attendance;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigdecimal.clasnapp.domain.student.Student;
import com.bigdecimal.clasnapp.domain.user.data.User;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByUser(User user);

    List<Attendance> findByStudent(Student student);
}
