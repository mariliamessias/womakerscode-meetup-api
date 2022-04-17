package com.womakerscode.meetup.model;

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

    private String publicPlace;

    private Integer number;

    private String zipCode;

    private String neighborhood;

    private String city;

    private String country;

    private LocalDateTime createdAt;
}
