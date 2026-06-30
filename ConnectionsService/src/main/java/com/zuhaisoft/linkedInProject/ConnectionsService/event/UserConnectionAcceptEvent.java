package com.zuhaisoft.linkedInProject.ConnectionsService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConnectionAcceptEvent {
    private Long receiverId;
    private Long senderId;
    private String name;
}
