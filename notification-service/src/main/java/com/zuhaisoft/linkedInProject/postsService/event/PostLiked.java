package com.zuhaisoft.linkedInProject.postsService.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLiked {

    private Long postId;
    private Long ownerUserId;
    private Long likedByUserId;

}
