package com.supercoding.backend_project_02.controller.comments;

import com.supercoding.backend_project_02.dto.comments.CommentDto;
import com.supercoding.backend_project_02.entity.comments.Comment;
import com.supercoding.backend_project_02.service.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    // private final JwtService jwtService;

    @GetMapping("/comments")
    public List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("/comments/{post_id}")
    public List<CommentEntity> findByPostid(@PathVariable long post_id) {
        return commentService.findByPostid(post_id);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO, @RequestHeader("Token") String token) {
        String author = jwtService.extractUserId(token);
        commentDTO.setAuthor(author); // Use the instance of CommentDTO

        commentService.saveComment(commentDTO);
        return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@PathVariable long id, @RequestBody CommentDTO commentDto, @RequestHeader("Token") String token) {
        String author = jwtService.extractUserId(token);

        if (author.equals(commentDto.getAuthor())) { // Use the instance of CommentDTO
            commentService.updateComment(commentDto, id);
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성한 댓글만 수정 가능합니다.");
        }
    }
}
