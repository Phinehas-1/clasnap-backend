package com.bigdecimal.clasnapp.domain.calendar;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bigdecimal.clasnapp.domain.user.data.User;



public interface CalendarRepository extends JpaRepository<Calendar,UUID>{
	List<Calendar> findByUser(User user);
	List<Calendar> findByUser(User user, Pageable pageable);
}
