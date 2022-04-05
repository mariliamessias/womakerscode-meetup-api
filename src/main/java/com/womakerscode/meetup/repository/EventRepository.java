package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
