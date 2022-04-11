package com.womakerscode.meetup.model;


import com.womakerscode.meetup.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest {

    @NotEmpty
    @Min(10)
    private String name;

    @NotEmpty
    private LocalDate birthDate;

    @NotEmpty
    @Email
    private String email;

    private AddressRequest address;

}
