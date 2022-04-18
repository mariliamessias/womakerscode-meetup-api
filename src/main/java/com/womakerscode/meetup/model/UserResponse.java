package com.womakerscode.meetup.model;

import com.womakerscode.meetup.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String userName;

    private LocalDateTime createdAt;

    private Role role;

    private List<RegistrationResponse> registrations;
}
