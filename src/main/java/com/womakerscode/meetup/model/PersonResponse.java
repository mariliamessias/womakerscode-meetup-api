package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PersonResponse {

    private Long id;

    private String name;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    private String email;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private AddressResponse address;
}
