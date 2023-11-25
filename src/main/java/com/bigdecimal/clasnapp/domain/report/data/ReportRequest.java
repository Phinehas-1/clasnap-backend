package com.bigdecimal.clasnapp.domain.report.data;

import java.util.List;

public record ReportRequest(String week, String topic, String summary, List<String> attendanceIds) {}
