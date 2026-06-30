package com.zuhaisoft.linkedInProject.postsService.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PersonDto {
    private Long id;

    private Long userId;

    private String name;
}
