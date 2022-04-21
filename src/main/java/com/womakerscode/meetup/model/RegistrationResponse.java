package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RegistrationResponse {

    private Long id;

    private Status status;

    private String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("event_name")
    private String eventName;

}
