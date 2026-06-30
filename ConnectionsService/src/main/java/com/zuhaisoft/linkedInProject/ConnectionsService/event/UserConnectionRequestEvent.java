package com.zuhaisoft.linkedInProject.ConnectionsService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConnectionRequestEvent {
    private Long receiverId;
    private Long senderId;
    private String name;
}
