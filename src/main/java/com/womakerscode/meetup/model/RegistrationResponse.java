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
public class RegistrationResponse {

    private Long id;

    private Status status;

    private String description;

    private LocalDateTime createdAt;

    private String userName;

    private String eventName;

}
