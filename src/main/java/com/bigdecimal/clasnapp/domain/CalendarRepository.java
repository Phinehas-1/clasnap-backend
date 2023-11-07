package com.bigdecimal.clasnapp.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CalendarRepository extends JpaRepository<Calendar,UUID>{
	List<Calendar> findByUser(User user);
	List<Calendar> findByUser(User user, Pageable pageable);
}
