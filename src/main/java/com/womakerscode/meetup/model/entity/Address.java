package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
public class Address {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_place")
    private String publicPlace;

    @Column
    private Integer number;

    @Column(name = "zip_code")
    private String zipCode;

    @Column
    private String neighborhood;

    @Column
    private String city;

    @Column
    private String country;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AddressResponse toAddresResponse() {
        return AddressResponse.builder()
                .city(city)
                .country(country)
                .createdAt(createdAt)
                .neighborhood(neighborhood)
                .number(number)
                .publicPlace(publicPlace)
                .zipCode(zipCode)
                .build();
    }
}
