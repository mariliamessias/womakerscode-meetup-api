package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.EventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Event {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private Status status;

    @Column
    private Integer maximunSpots;

    @Column
    private Integer alocatedSpots;

    public EventResponse toEventResponse() {
       return EventResponse.builder()
               .name(name)
               .id(id)
               .status(status)
               .maximunSpots(maximunSpots)
               .alocatedSpots(alocatedSpots)
               .createdAt(createdAt)
               .build();
    }
}
