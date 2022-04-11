package com.womakerscode.meetup.model;

import com.womakerscode.meetup.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private LocalDateTime createdAt;

    @NotEmpty
    private Status status;

    @NotEmpty
    @Min(1)
    private Integer maximunSpots;

    @NotEmpty
    private Integer alocatedSpots;

}
