package com.supercoding.backend_project_02.service.posts;

import com.supercoding.backend_project_02.dto.posts.Post;
import com.supercoding.backend_project_02.dto.posts.PostBody;
import com.supercoding.backend_project_02.entity.posts.PostsEntity;
import com.supercoding.backend_project_02.entity.users.Users;
import com.supercoding.backend_project_02.repository.posts.PostRepository;
import com.supercoding.backend_project_02.repository.users.UserRepository;
import com.supercoding.backend_project_02.service.posts.exception.ResourceNotFoundException;
import com.supercoding.backend_project_02.service.posts.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 전체 조회
    public List<Post> findAll(){
        List<PostsEntity> postsEntities = postRepository.findAll();
        return postsEntities.stream()
                            .map(PostMapper.INSTANCE::postEntityToPost)
                            .collect(Collectors.toList());
    }

    // 작성자 이메일로 게시물 조회
    public List<Post> getPostsByAuthorEmail(String email) {
        List<PostsEntity> postsEntities = postRepository.findByUserEmail(email);
        if (postsEntities.isEmpty()) {
            throw new ResourceNotFoundException("게시물을 찾을 수 없습니다.");
        }
        return postsEntities.stream()
                            .map(PostMapper.INSTANCE::postEntityToPost)
                            .collect(Collectors.toList());
    }

    // 게시물 생성
    public void createPost(PostBody postBody, String authorEmail) {
        // 이메일로 사용자 찾기
        Optional<Users> optionalUser = userRepository.findByEmail(authorEmail);

        Users user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // 사용자가 존재하지 않으면 새 사용자 생성
            user = new Users();
            user.setEmail(authorEmail);
            userRepository.save(user);
        }

        // PostBody를 PostsEntity로 매핑
        PostsEntity postsEntity = PostMapper.INSTANCE.postBodyToPostEntity(postBody);
        postsEntity.setUser(user);

        // 게시물 엔티티 저장
        postRepository.save(postsEntity);
    }

    // 게시물 수정하기
    public void updatePost(Integer postId, Post post) {
        PostsEntity existingPost = postRepository.findById(postId)
                                                 .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다."));
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postRepository.save(existingPost);
    }

    // 받은 postId 게시물 삭제
    public void deletePost(Integer postId) {
        PostsEntity existingPost = postRepository.findById(postId)
                                                 .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다."));
        postRepository.delete(existingPost);
    }
}