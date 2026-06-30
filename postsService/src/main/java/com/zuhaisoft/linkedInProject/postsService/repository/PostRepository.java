package com.zuhaisoft.linkedInProject.postsService.repository;

import com.zuhaisoft.linkedInProject.postsService.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
