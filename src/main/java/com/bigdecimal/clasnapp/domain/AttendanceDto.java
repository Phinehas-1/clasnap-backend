package com.bigdecimal.clasnapp.domain;

import java.util.List;

public record AttendanceDto(String week, List<String> studentIds) {
    
}
