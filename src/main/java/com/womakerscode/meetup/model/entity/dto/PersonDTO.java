package com.womakerscode.meetup.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDTO {

    private Long id;

    @NotEmpty
    @Min(10)
    private String name;

    @NotEmpty
    private LocalDate birthDate;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private LocalDateTime createdAt;

}
