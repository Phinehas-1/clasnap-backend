package com.bigdecimal.clasnapp.domain.attendance;

public record AttendanceRequest(
  String calendarId,
  String studentId,
  String attendanceId,
  Boolean laptop,
  Long score
) {}
