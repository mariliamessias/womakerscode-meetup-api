package com.womakerscode.meetup.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SendEmaillMessage {
    private String messageId;
    private String email;
    private String type;
    private String eventName;
    private String name;
    private String eventDate;
}
