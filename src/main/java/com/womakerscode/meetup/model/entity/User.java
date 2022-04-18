package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    private Role role;

    @Column(name = "user_name")
    private String userName;

    @Column
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Registration> registrations = new ArrayList<>();

    public UserResponse toUserResponse() {
        return UserResponse.builder()
                .createdAt(createdAt)
                .userName(userName)
                .registrations(Optional.ofNullable(registrations)
                        .map(response -> response.stream().map(Registration::toRegistrationResponse).collect(Collectors.toList()))
                        .orElse(new ArrayList<>()))
                .role(role)
                .id(id)
                .build();
    }


}
