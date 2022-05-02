package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.EventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column
    private Status status;

    @Column(name = "maximun_spots")
    private Integer maximunSpots;

    @Column(name = "alocated_spots")
    private Integer alocatedSpots;

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations = new ArrayList<>();

    public EventResponse toEventResponse() {
        return EventResponse.builder()
                .name(name)
                .id(id)
                .status(status)
                .registrations(Optional.ofNullable(registrations)
                        .map(response -> response.stream().map(Registration::toRegistrationResponse).collect(Collectors.toList()))
                        .orElse(new ArrayList<>()))
                .maximunSpots(maximunSpots)
                .alocatedSpots(alocatedSpots)
                .createdAt(createdAt)
                .build();
    }
}
