package com.zuhaisoft.linkedInProject.connectionsService.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserConnectionRequestEvent {
    private Long receiverId;
    private Long senderId;
    private String name;
}
