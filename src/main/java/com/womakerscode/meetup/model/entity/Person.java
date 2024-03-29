package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.PersonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Person {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "user_name")
    private String username;

    public PersonResponse toPersonResponse() {
        return PersonResponse.builder()
                .createdAt(createdAt)
                .address(address.toAddresResponse())
                .birthDate(birthDate)
                .email(email)
                .name(name)
                .id(id)
                .build();
    }
}
