package com.bigdecimal.clasnapp.report.attendance;

import java.util.List;

public record AttendanceDto(String week, List<String> studentIds) {
    
}
