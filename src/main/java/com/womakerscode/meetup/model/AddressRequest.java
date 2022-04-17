package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Address;
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
public class AddressRequest {

    @NotEmpty
    @JsonProperty("public_place")
    private String publicPlace;

    @NotEmpty
    private Integer number;

    @NotEmpty
    @JsonProperty("zip_code")
    private String zipCode;

    @NotEmpty
    private String neighborhood;

    @NotEmpty
    private String city;

    @NotEmpty
    private String country;

    public Address toSaveAddress() {
        return Address.builder()
                .createdAt(LocalDateTime.now())
                .neighborhood(neighborhood)
                .publicPlace(publicPlace)
                .number(number)
                .city(city)
                .zipCode(zipCode)
                .country(country)
                .build();
    }
}
