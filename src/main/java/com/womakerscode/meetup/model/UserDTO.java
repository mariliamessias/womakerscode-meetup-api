package com.womakerscode.meetup.model;

import com.womakerscode.meetup.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotEmpty
    private Role role;

    @NotEmpty
    @Max(50)
    private String userName;

    @NotEmpty
    private String password;

    @NotEmpty
    private LocalDate createdAt;

}
