package com.bigdecimal.clasnapp.report;

import com.bigdecimal.clasnapp.domain.Attendance;
import com.bigdecimal.clasnapp.domain.AttendanceDto;
import com.bigdecimal.clasnapp.domain.AttendanceRepository;
import com.bigdecimal.clasnapp.domain.Group;
import com.bigdecimal.clasnapp.domain.Report;
import com.bigdecimal.clasnapp.domain.ReportDto;
import com.bigdecimal.clasnapp.domain.ReportRepository;
import com.bigdecimal.clasnapp.domain.Student;
import com.bigdecimal.clasnapp.domain.StudentDto;
import com.bigdecimal.clasnapp.domain.StudentRepository;
import com.bigdecimal.clasnapp.domain.User;
import com.bigdecimal.clasnapp.domain.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Transactional
  public List<Student> addStudents(List<StudentDto> studentDtos) {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);

    List<Student> studentList = studentDtos
      .stream()
      .map(studentDto -> {
        Student student = new Student(
          studentDto.name(),
          studentDto.laptop(),
          studentDto.score()
        );
        student.setUser(user);
        return student;
      })
      .toList();
    return students.saveAll(studentList);
  }

  @Transactional
  public List<Student> fetchAllStudentsByUser() {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
    return students.findAllByUser(user);
  }

  @Transactional
  public void deleteStudentById(String studentId) {
    students.deleteById(UUID.fromString(studentId));
  }

  @Transactional
  public Attendance createAttendance(AttendanceDto attendanceDto) {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
    List<UUID> ids = attendanceDto
      .studentIds()
      .stream()
      .map(studentId -> UUID.fromString(studentId))
      .toList();
    List<Student> studentList = new ArrayList<Student>();
    ids.forEach(id ->
      studentList.add(
        students.findById(id).orElseThrow(EntityNotFoundException::new)
      )
    );
    Attendance attendance = new Attendance(attendanceDto.week());
    attendance.setStudents(studentList);
    attendance.setUser(user);
    return attendances.save(attendance);
  }

  @Transactional
  public List<Attendance> fetchAllAttendancesByUser() {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
    return attendances.findAllByUser(user);
  }

  @Transactional
  public Report createReport(ReportDto reportDto, String attendanceId) {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
    Attendance attendance = attendances
      .findById(UUID.fromString(attendanceId))
      .orElseThrow(EntityNotFoundException::new);
    Report report = new Report(
      user,
      reportDto.week(),
      reportDto.topic(),
      reportDto.summary()
    );
    report.setSubmittedOn(Timestamp.from(Instant.now()));
    report.setUser(user);
    report.setAttendance(attendance);
    return reports.save(report);
  }

  @Transactional
  public List<Report> fetchAllReportsByUser() {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    User user = users
      .findByUsername(username)
      .orElseThrow(EntityNotFoundException::new);
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
  // TODO Implement update Attendance, update Report.
}
