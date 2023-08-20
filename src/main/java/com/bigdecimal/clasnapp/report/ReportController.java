package com.bigdecimal.clasnapp.report;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigdecimal.clasnapp.domain.AttendanceDto;
import com.bigdecimal.clasnapp.domain.ReportDto;
import com.bigdecimal.clasnapp.domain.StudentDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports Management")
public class ReportController {

  private final ReportService reportService;

  @Secured("USER")
  @PostMapping("/students")
  @Operation(
    description = "Post endpoint for creating a student or list of students.",
    responses = { @ApiResponse(description = "Success", responseCode = "200") }
  )
  public ResponseEntity<?> createStudent(
    @RequestBody List<StudentDto> studentList
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.addStudents(studentList));
  }

  @Secured("USER")
  @GetMapping("/students")
  public ResponseEntity<?> getAllStudents() {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .body(reportService.fetchAllStudentsByUser());
  }

  @Secured("USER")
  @DeleteMapping("/students/{studentId}")
  public ResponseEntity<?> deleteStudent(
    @PathVariable("studentId") String studentId
  ) {
    reportService.deleteStudentById(studentId);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .body(studentId + " deleted.");
  }

  @Secured("USER")
  @PostMapping("/attendance")
  public ResponseEntity<?> createAttendance(
    @RequestBody AttendanceDto attendanceDto
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.createAttendance(attendanceDto));
  }

  @Secured("USER")
  @GetMapping("/attendances")
  public ResponseEntity<?> getAllAttendancesByUser() {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .body(reportService.fetchAllAttendancesByUser());
  }

  @Secured("USER")
  @PostMapping("/")
  public ResponseEntity<?> createReport(
    @RequestBody ReportDto reportDto,
    @RequestParam String attendanceId
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.createReport(reportDto, attendanceId));
  }

  @Secured("USER")
  @GetMapping("/reports")
  public ResponseEntity<?> getAllReportsByUser() {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .body(reportService.fetchAllReportsByUser());
  }

  @Secured("GROUP_LEADER")
  @GetMapping("/group/reports")
  public ResponseEntity<?> getAllReportsByGroup() {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .body(reportService.fetchAllReportsByGroup());
  }
}
