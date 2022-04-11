package com.womakerscode.meetup.model;

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

    private LocalDate birthDate;

    private String email;

    private LocalDateTime createdAt;

    private AddressResponse address;
}
