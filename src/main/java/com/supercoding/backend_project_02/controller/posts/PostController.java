package com.supercoding.backend_project_02.controller.posts;

import com.supercoding.backend_project_02.Token.JwtProvider;
import com.supercoding.backend_project_02.dto.posts.Post;
import com.supercoding.backend_project_02.dto.posts.PostBody;
import com.supercoding.backend_project_02.dto.posts.PostResponse;
import com.supercoding.backend_project_02.service.posts.PostService;
import com.supercoding.backend_project_02.service.posts.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;


    // 게시물 전체 조회
    @GetMapping("/posts")
    public PostResponse findAll() {
        List<Post> posts = postService.findAll();
        return new PostResponse(posts);
    }

    // 작성자 이메일로 게시물 검색
    @GetMapping("/posts/search")
    public ResponseEntity<Map<String, List<Post>>> getPostsByAuthorEmail(@RequestParam("author_email") String email) {
        List<Post> posts = postService.getPostsByAuthorEmail(email);
        Map<String, List<Post>> response = new HashMap<>();
        response.put("posts", posts);
        return ResponseEntity.ok(response);
    }

    // 예외 처리
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 게시물 생성
    @PostMapping("/posts")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody PostBody postBody) {
        String authorEmail = postBody.getAuthor();

        try {
            // Author로부터 토큰 생성
            String token = jwtProvider.getTokenForEmail(authorEmail);
            String emailFromToken = jwtProvider.getEmailFromToken(token);
            if (!emailFromToken.equals(authorEmail)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Author email does not match with token email"));
            }

            postService.createPost(postBody, emailFromToken);
            return ResponseEntity.ok(Map.of("message", "게시물이 성공적으로 작성되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid JWT token"));
        }
    }

    // 게시물 수정
    @PutMapping("/posts/{post_id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable("post_id") Integer postId, @RequestBody Post post) {
        postService.updatePost(postId, post);
        Map<String, String> response = new HashMap<>();
        response.put("message", "게시물이 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 게시물 삭제
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable("post_id") Integer postId) {
        postService.deletePost(postId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "게시물이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }
}
