package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AddressResponse {

    @JsonProperty("public_place")
    private String publicPlace;

    private Integer number;

    @JsonProperty("zip_code")
    private String zipCode;

    private String neighborhood;

    private String city;

    private String country;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
