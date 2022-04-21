package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private Long id;

    private String name;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private Status status;

    @JsonProperty("maximun_spots")
    private Integer maximunSpots;

    @JsonProperty("alocated_spots")
    private Integer alocatedSpots;

    private List<RegistrationResponse> registrations;
}
