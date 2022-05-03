package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Transactional
    Integer deleteEventById(Long id);

    List<Event> findEventByStatus(Status status);

}
