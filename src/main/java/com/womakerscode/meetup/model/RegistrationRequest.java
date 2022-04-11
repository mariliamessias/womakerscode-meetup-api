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
public class RegistrationRequest {

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("event_id")
    private Long eventId;

    private Status status = Status.ACTIVE;

    public Registration toSaveRegistration(User user, Event event) {
        return Registration.builder()
                .status(CREATED)
                .user(user)
                .event(event)
                .createdAt(LocalDateTime.now())
                .description(description)
                .build();
    }

    public Registration toFindRegistration() {
        return Registration.builder()
                .status(status)
                .description(description)
                .build();
    }

}
