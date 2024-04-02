package com.kh.Final_Project.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kh.Final_Project.event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
	// 이벤트 전체
	@Query(value = "SELECT * FROM Event ORDER BY ocid DESC", nativeQuery = true)
	Page<Event> findAll(Pageable pageable);	
	
	}
