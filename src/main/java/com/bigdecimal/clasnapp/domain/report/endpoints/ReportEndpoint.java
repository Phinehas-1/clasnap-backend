package com.bigdecimal.clasnapp.domain.report.endpoints;

import com.bigdecimal.clasnapp.domain.attendance.AttendanceRequest;
import com.bigdecimal.clasnapp.domain.attendance.UpdateAttendanceRequest;
import com.bigdecimal.clasnapp.domain.calendar.CalendarRequest;
import com.bigdecimal.clasnapp.domain.report.data.ReportRequest;
import com.bigdecimal.clasnapp.domain.report.services.ReportService;
import com.bigdecimal.clasnapp.domain.student.StudentRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports Management")
@Slf4j
public class ReportEndpoint {

  private final ReportService reportService;

  @Secured("USER")
  @PostMapping("/students")
  @Operation(
    description = "Create a list of students.",
    responses = { @ApiResponse(description = "Success", responseCode = "200") }
  )
  public ResponseEntity<?> createStudent(
    @RequestBody List<StudentRequest> studentList
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.addStudents(studentList));
  }

  @Secured("USER")
  @GetMapping("/students")
  public ResponseEntity<?> getAllStudents() {
    return ResponseEntity
      .status(HttpStatus.OK)
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
  @Operation(description = "Create a calendar for each week.")
  @PostMapping("/calendars")
  public ResponseEntity<?> createCalendar(
    @RequestBody CalendarRequest calendarDto
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.createCalendar(calendarDto));
  }

  @Secured("USER")
  @Operation(description = "Fetches all calendar for the logged in user.")
  @GetMapping("/calendars")
  public ResponseEntity<?> fetchCalendar() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(reportService.fetchAllCalendarsByUser());
  }

  @Secured("USER")
  @Operation(
    description = "Fetches all calendar for the logged in user, sorting by created_at from latest to earliest."
  )
  @GetMapping("/calendars/sorted")
  public ResponseEntity<?> fetchCalendarSorted() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(reportService.fetchUserCalendarByCreatedAtSorted());
  }

  @Secured("USER")
  @PostMapping("/attendance")
  public ResponseEntity<?> createAttendance(
    @RequestBody AttendanceRequest attendanceDto
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.createAttendance(attendanceDto));
  }

  @Secured("USER")
  @PutMapping("/attendance")
  public ResponseEntity<?> updateAttendance(
    @RequestBody UpdateAttendanceRequest updateAttendanceDto
  ) {
    return ResponseEntity
      .status(HttpStatus.ACCEPTED)
      .body(reportService.updateAttendance(updateAttendanceDto));
  }

  @Secured("USER")
  @GetMapping("/attendances/{calendarId}")
  public ResponseEntity<?> getAllAttendancesByUser(
    @PathVariable("calendarId") String calendarId
  ) {
    log.info("calender id is {}", calendarId);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(reportService.fetchAllUserAttendancesByWeek(calendarId));
  }

  @Secured("USER")
  @PostMapping("/")
  public ResponseEntity<?> createReport(@RequestBody ReportRequest reportRequest) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reportService.createReport(reportRequest));
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
