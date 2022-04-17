package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public UserResponse toUserResponse() {
       return UserResponse.builder()
               .createdAt(createdAt)
               .userName(userName)
               .role(role)
               .build();
    }


}
