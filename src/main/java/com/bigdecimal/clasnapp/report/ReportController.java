package com.bigdecimal.clasnapp.report;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigdecimal.clasnapp.report.attendance.AttendanceDto;
import com.bigdecimal.clasnapp.report.student.Student;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody List<Student> studentList) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.registerStudents(studentList));
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> createAttendance(@RequestBody AttendanceDto attendanceDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createAttendance(attendanceDto));
    }

    @PostMapping("/report/{attendanceId}")
    public ResponseEntity<?> createReport(@RequestBody Report report, @PathVariable String attendanceId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(report, attendanceId));
    }
}
