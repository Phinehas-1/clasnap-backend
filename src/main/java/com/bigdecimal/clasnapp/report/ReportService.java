package com.bigdecimal.clasnapp.report;

import com.bigdecimal.clasnapp.domain.Attendance;
import com.bigdecimal.clasnapp.domain.AttendanceDto;
import com.bigdecimal.clasnapp.domain.AttendanceRepository;
import com.bigdecimal.clasnapp.domain.Calendar;
import com.bigdecimal.clasnapp.domain.CalendarDto;
import com.bigdecimal.clasnapp.domain.CalendarRepository;
import com.bigdecimal.clasnapp.domain.Group;
import com.bigdecimal.clasnapp.domain.Report;
import com.bigdecimal.clasnapp.domain.ReportDto;
import com.bigdecimal.clasnapp.domain.ReportRepository;
import com.bigdecimal.clasnapp.domain.Student;
import com.bigdecimal.clasnapp.domain.StudentDto;
import com.bigdecimal.clasnapp.domain.StudentRepository;
import com.bigdecimal.clasnapp.domain.UpdateAttendanceDto;
import com.bigdecimal.clasnapp.domain.User;
import com.bigdecimal.clasnapp.domain.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

  private final ReportRepository reports;
  private final AttendanceRepository attendances;
  private final StudentRepository students;
  private final UserRepository users;
  private final CalendarRepository calendars;

  @Transactional
  public List<Student> addStudents(List<StudentDto> studentDtos) {
    User user = getLoggedInUser();
    List<Student> studentList = studentDtos
      .stream()
      .map(studentDto -> {
        Student student = new Student(studentDto.name());
        student.setUser(user);
        return student;
      })
      .toList();
    return students.saveAll(studentList);
  }

  @Transactional
  public List<Student> fetchAllStudentsByUser() {
    User user = getLoggedInUser();
    return students.findAllByUser(user);
  }

  @Transactional
  public void deleteStudentById(String studentId) {
    students.deleteById(UUID.fromString(studentId));
  }

  @Transactional
  public Calendar createCalendar(CalendarDto calendarDto) {
    User user = getLoggedInUser();
    String weekName = user.getUsername().concat("_" + calendarDto.week());
    Calendar calendar = new Calendar(calendarDto.week(), weekName);
    calendar.setUser(user);
    return calendars.save(calendar);
  }

  @Transactional
  public List<Calendar> fetchAllCalendarsByUser() {
    User user = getLoggedInUser();
    return calendars.findByUser(user);
  }

  @Transactional
  public List<Calendar> fetchUserCalendarByCreatedAtSorted() {
    User user = getLoggedInUser();
    Pageable pageable = PageRequest.of(
      0,
      Integer.MAX_VALUE,
      Direction.DESC,
      "createdAt"
    );
    return calendars.findByUser(user, pageable);
  }

  @Transactional
  public Attendance createAttendance(AttendanceDto attendanceDto) {
    User user = getLoggedInUser();
    Student student = students
      .findById(UUID.fromString(attendanceDto.studentId()))
      .orElseThrow(EntityNotFoundException::new);
    List<Attendance> attendanceList = attendances.findByStudent(student);
    if (
      attendanceList
        .stream()
        .anyMatch(attendance ->
          attendance
            .getCalendar()
            .getId()
            .equals(UUID.fromString(attendanceDto.calendarId()))
        )
    ) {
      throw new DataIntegrityViolationException(
        "Duplicate attendance for the same week not allowed."
      );
    }
    Calendar calendar = calendars
      .findById(UUID.fromString(attendanceDto.calendarId()))
      .orElseThrow(EntityNotFoundException::new);
    Attendance attendance = new Attendance(calendar);
    attendance.setCalendar(calendar);
    attendance.setStudent(student);
    attendance.setUser(user);
    return attendances.save(attendance);
  }

  @Transactional
  public Attendance updateAttendance(UpdateAttendanceDto updateAttendanceDto) {
    Attendance attendance = attendances
      .findById(UUID.fromString(updateAttendanceDto.attendanceId()))
      .orElseThrow(() ->
        new EntityNotFoundException("Attendance record doesn't exist")
      );
    attendance.setLaptop(updateAttendanceDto.laptop());
    attendance.setScore(updateAttendanceDto.score());
    attendances.save(attendance);
    return attendance;
  }

  @Transactional
  public List<Attendance> fetchAllUserAttendancesByWeek(String calendarId) {
    User user = getLoggedInUser();
    List<Attendance> attendanceList = attendances.findAllByUser(user);
    return attendanceList
      .stream()
      .filter(attendance ->
        calendarId.equals(attendance.getCalendar().getId().toString())
      )
      .toList();
  }

  @Transactional
  public Report createReport(ReportDto reportDto) {
    User user = getLoggedInUser();
    List<Attendance> attendanceList = reportDto
      .attendanceIds()
      .stream()
      .map(attendanceId ->
        attendances
          .findById(UUID.fromString(attendanceId))
          .orElseThrow(EntityNotFoundException::new)
      )
      .toList();
    Report report = new Report(
      user,
      reportDto.week(),
      reportDto.topic(),
      reportDto.summary()
    );
    report.setSubmittedOn(Timestamp.from(Instant.now()));
    report.setUser(user);
    report.setAttendances(attendanceList);
    return reports.save(report);
  }

  @Transactional
  public List<Report> fetchAllReportsByUser() {
    User user = getLoggedInUser();
    return reports.findAllByUser(user);
  }

  @Transactional
  public List<List<Report>> fetchAllReportsByGroup() {
    Long startTime = System.currentTimeMillis();
    // find the group of the principal
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    Group userGroup = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new)
      .getGroup();
    log.info(
      "User < {} > attempt fetching the last 2 reports submitted by members of group < {} >.",
      username,
      userGroup.getName()
    );
    // fetch the user ids for all users that belongs in the same group as the
    // current user.
    List<UUID> userIds = users
      .findAllByGroup(userGroup)
      .stream()
      .map(user -> user.getId())
      .toList();
    // fetch the last 2 submitted reports for each user found.
    Pageable pageable = PageRequest.of(0, 2, Direction.ASC, "submitted_on");
    List<List<Report>> reportList = userIds
      .stream()
      .map(userId -> reports.findAllByUserIdSorted(userId, pageable))
      .toList();
    log.info(
      "User < {} > fetched their group reports in {} ms",
      username,
      System.currentTimeMillis() - startTime
    );
    return reportList;
  }

  @Transactional
  private User getLoggedInUser() {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    return users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
  }
  // TODO Implement update Attendance, update Report.
}
