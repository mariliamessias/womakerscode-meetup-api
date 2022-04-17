package com.womakerscode.meetup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Role role;

    @NotEmpty
    @Max(50)
    @JsonProperty("user_name")
    private String userName;

    @NotEmpty
    @Max(50)
    private String password;

    private Long personId;

    public User toSaveUser(Person person) {
        return User.builder()
                .userName(userName)
                .password(password)
                .role(role)
                .person(person)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
