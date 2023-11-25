package com.bigdecimal.clasnapp.domain.attendance;

public record UpdateAttendanceRequest(
  String attendanceId,
  Boolean laptop,
  Long score
) {}
