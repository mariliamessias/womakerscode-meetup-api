package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.womakerscode.meetup.model.entity.Status.CREATED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequest {

    private String name;

    private Status status;

    @JsonProperty("maximun_spots")
    private Integer maximunSpots;

    @JsonProperty("alocated_spots")
    private Integer alocatedSpots;

    @JsonProperty("event_date")
    private LocalDate eventDate;

    public Event toSaveEvent() {
        return Event.builder()
                .status(CREATED)
                .createdAt(LocalDateTime.now())
                .name(name)
                .eventDate(eventDate)
                .maximunSpots(maximunSpots)
                .alocatedSpots(alocatedSpots)
                .build();
    }

}
