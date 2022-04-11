package com.womakerscode.meetup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    @NotEmpty
    private String publicPlace;

    @NotEmpty
    private Integer number;

    @NotEmpty
    private String zipCode;

    @NotEmpty
    private String neighborhood;

    @NotEmpty
    private String city;

    @NotEmpty
    private String country;

}
