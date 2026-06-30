package com.zuhaisoft.linkedInProject.connectionsService.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserConnectionAcceptEvent {
    private Long receiverId;
    private Long senderId;
    private String name;
}
