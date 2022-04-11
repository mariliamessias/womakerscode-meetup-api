package com.womakerscode.meetup.model.entity;

import com.womakerscode.meetup.model.RegistrationResponse;
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
public class Registration {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Status status;

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public RegistrationResponse toRegistrationResponse() {
        return RegistrationResponse.builder()
                .description(description)
                .createdAt(createdAt)
                .id(id)
                .status(status)
                .build();
    }

}
