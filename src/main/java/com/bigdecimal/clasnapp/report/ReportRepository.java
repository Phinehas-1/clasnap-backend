package com.bigdecimal.clasnapp.report;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    
}
