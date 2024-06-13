package com.supercoding.backend_project_02.dto.comments;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
//    private Long postId;
//    private Long userId;

    public CommentDto(Long id, String content, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
//        this.postId = postId;
//        this.userId = userId;
    }

}

