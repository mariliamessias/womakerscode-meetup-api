package com.womakerscode.meetup.model;

import com.womakerscode.meetup.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String userName;

    private LocalDateTime createdAt;

    private Role role;
}
