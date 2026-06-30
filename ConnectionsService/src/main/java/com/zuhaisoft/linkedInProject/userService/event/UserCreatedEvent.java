package com.zuhaisoft.linkedInProject.userService.event;

import lombok.*;

@Data
public class UserCreatedEvent {
    private Long userId;
    private String name;
}
