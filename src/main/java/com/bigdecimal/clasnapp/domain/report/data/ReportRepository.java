package com.bigdecimal.clasnapp.domain.report.data;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bigdecimal.clasnapp.domain.user.data.User;

import io.lettuce.core.dynamic.annotation.Param;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    public List<Report> findAllByUser(User user);

    @Query(value = "SELECT * FROM report r WHERE r.user_id = :userId", nativeQuery = true)
    public List<Report> findAllByUserIdSorted(@Param("userId") UUID userId, Pageable pageable);
}
