package com.womakerscode.meetup.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest {

    @NotEmpty
    @Size(min = 10, message = "Attribute name must have more than 10 characters")
    private String name;

    @NotNull
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private AddressRequest address;

    public Person toSavePerson(Address address) {
        return Person.builder()
                .createdAt(LocalDateTime.now())
                .email(email)
                .name(name)
                .address(address)
                .birthDate(birthDate)
                .build();
    }
}
