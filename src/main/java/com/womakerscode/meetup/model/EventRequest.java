package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    public Event toSaveEvent() {
        return Event.builder()
                .status(CREATED)
                .createdAt(LocalDateTime.now())
                .name(name)
                .maximunSpots(maximunSpots)
                .alocatedSpots(alocatedSpots)
                .build();
    }

}
