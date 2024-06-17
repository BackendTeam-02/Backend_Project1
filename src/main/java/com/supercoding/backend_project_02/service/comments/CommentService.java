package com.supercoding.backend_project_02.service.comments;

import com.supercoding.backend_project_02.dto.comments.CommentDto;
import com.supercoding.backend_project_02.entity.comments.Comment;
import com.supercoding.backend_project_02.entity.posts.PostsEntity;
import com.supercoding.backend_project_02.repository.comments.CommentRepository;
import com.supercoding.backend_project_02.repository.posts.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<CommentDto> findByPostId(long postId) {
        return commentRepository.findAllByPost_Id(postId)
                                .stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
    }

    public List<CommentDto> findAll() {
        return commentRepository.findAll()
                                .stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
    }

    public void saveComment(CommentDto commentDto) {
        System.out.println("Saving comment for post_id: " + commentDto.getPost_id());

        PostsEntity postsEntity = postRepository.findById(commentDto.getPost_id())
                                                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물을 찾을 수 없습니다."));

        Comment commentEntity = Comment.builder()
                                       .post(postsEntity)
                                       .author(commentDto.getAuthor())
                                       .content(commentDto.getContent())
                                       .created_at(LocalDateTime.now())
                                       .build();

        commentRepository.save(commentEntity);
    }

    public void updateCommentContent(long id, String content) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isPresent()) {
            Comment commentEntity = existingComment.get();
            commentEntity.setContent(content);
            commentRepository.save(commentEntity);
        } else {
            throw new EntityNotFoundException("댓글을 찾을 수 없습니다.");
        }
    }


    public void deleteComment(long id) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isPresent()) {
            commentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("댓글을 찾을 수 없습니다.");
        }
    }

    public String getCommentAuthor(long id) {
        return commentRepository.findById(id)
                                .map(Comment::getAuthor)
                                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }


    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreated_at()
        );
    }
}