package com.bigdecimal.clasnapp.domain;

import java.util.List;

public record ReportDto(String week, String topic, String summary, List<String> attendanceIds) {}
