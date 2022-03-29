package com.womakerscode.meetup.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private LocalDateTime dateOfRegistration;

    @NotEmpty
    private String registration;
}
