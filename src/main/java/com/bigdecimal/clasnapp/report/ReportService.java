package com.bigdecimal.clasnapp.report;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bigdecimal.clasnapp.report.attendance.Attendance;
import com.bigdecimal.clasnapp.report.attendance.AttendanceDto;
import com.bigdecimal.clasnapp.report.attendance.AttendanceRepository;
import com.bigdecimal.clasnapp.report.student.Student;
import com.bigdecimal.clasnapp.report.student.StudentRepository;
import com.bigdecimal.clasnapp.user.User;
import com.bigdecimal.clasnapp.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reports;
    private final AttendanceRepository attendances;
    private final StudentRepository students;
    private final UserRepository users;

    @Transactional
    public List<Student> registerStudents(List<Student> studentList) {
        return students.saveAll(studentList);
    }

    @Transactional
    public void deleteStudentById(String studentId) {
        students.deleteById(UUID.fromString(studentId));
    }

    @Transactional
    public List<Student> fetchStudentsByUser(String userId) {
        User user = users.findById(UUID.fromString(userId)).orElseThrow(NullPointerException::new);
        return students.findAllByUser(user);
    }

    @Transactional
    public Attendance createAttendance(AttendanceDto attendanceDto) {
        List<UUID> ids = attendanceDto.studentIds().stream().map(studentId -> UUID.fromString(studentId)).toList();
        List<Student> studentList = students.findAllById(ids);
        Attendance attendance = new Attendance(attendanceDto.week());
        attendance.setStudents(studentList);
        return attendances.save(attendance);
    }

    @Transactional
    public List<Attendance> fetchAttendancesByUser(String userId) {
        User user = users.findById(UUID.fromString(userId)).orElseThrow(NullPointerException::new);
        return attendances.findAllByUser(user);
    }

    @Transactional
    public Report createReport(Report report, String attendanceId) {
        Attendance attendance = attendances.findById(UUID.fromString(attendanceId))
                .orElseThrow(NullPointerException::new);
        report.setAttendance(attendance);
        return reports.save(report);
    }
}
