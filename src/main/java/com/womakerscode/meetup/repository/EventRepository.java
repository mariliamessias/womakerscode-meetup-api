package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Transactional
    Integer deleteEventById(Long id);
}
