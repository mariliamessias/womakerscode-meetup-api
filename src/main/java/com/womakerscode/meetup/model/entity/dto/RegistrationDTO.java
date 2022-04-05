package com.womakerscode.meetup.model.entity.dto;

import com.womakerscode.meetup.model.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {

    private Long id;

    @NotEmpty
    @Min(10)
    private String description;

    @NotEmpty
    private LocalDateTime createdAt;

    public Registration toRegistration(){
        return Registration.builder()
                .id(id)
                .createdAt(createdAt)
                .description(description)
                .build();
    }

}
