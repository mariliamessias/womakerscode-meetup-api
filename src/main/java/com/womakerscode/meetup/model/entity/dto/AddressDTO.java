package com.womakerscode.meetup.model.entity.dto;

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
public class AddressDTO {

    private Long id;

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

    @NotEmpty
    private LocalDateTime createdAt;

}
