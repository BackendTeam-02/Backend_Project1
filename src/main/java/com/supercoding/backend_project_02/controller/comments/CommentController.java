package com.supercoding.backend_project_02.controller.comments;

import com.supercoding.backend_project_02.Token.JwtProvider;
import com.supercoding.backend_project_02.dto.comments.CommentDto;
import com.supercoding.backend_project_02.service.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    // 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> findAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<CommentDto>> findByPostId(@PathVariable long postId) {
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }

    // 댓글 생성
    @PostMapping("/comments")
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto) {
        commentService.saveComment(commentDto);
        return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
    }

    // 댓글 수정
    @PutMapping("/comments/{id}")
    public ResponseEntity<String> updateComment(@PathVariable("id") long id, @RequestBody Map<String, String> payload) {
        if (!payload.containsKey("content")) {
            return ResponseEntity.badRequest().body("Content field is missing in the request body.");
        }

        String content = payload.get("content");
        if (content == null || content.isEmpty()) {
            return ResponseEntity.badRequest().body("Content cannot be null or empty.");
        }

        commentService.updateCommentContent(id, content);
        return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }
}