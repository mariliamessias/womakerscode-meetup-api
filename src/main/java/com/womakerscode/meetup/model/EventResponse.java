package com.womakerscode.meetup.model;

import com.womakerscode.meetup.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private Status status;

    private Integer maximunSpots;

    private Integer alocatedSpots;
}
