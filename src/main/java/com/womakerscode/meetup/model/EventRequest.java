package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import static com.womakerscode.meetup.model.entity.Status.CREATED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequest {

    @NotEmpty
    private String name;

    private Status status = CREATED;

    @NotEmpty
    @JsonProperty("maximun_spots")
    private Integer maximunSpots;

    @NotEmpty
    @JsonProperty("alocated_spots")
    private Integer alocatedSpots;
}
