package com.supercoding.backend_project_02.service.comments;


import com.supercoding.backend_project_02.dto.comments.CommentDto;
import com.supercoding.backend_project_02.entity.comments.Comment;
import com.supercoding.backend_project_02.repository.comments.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository CommentRepository;
   // private final PostRepository postRepository;

    public List<Comment> findByPostid(long postId) {
        return CommentRepository.findAllByPost_Id(postId);
    }

    public List<Comment> findAll() {
        return CommentRepository.findAllBy();
    }

    public void saveComment(CommentDto commentDTO) {
        PostEntity post = postRepository.findById(commentDTO.getPost_id())
                                        .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물을 찾을 수 없습니다."));

        CommentEntity CommentEntity = TestPJ.entity.CommentEntity.builder()
                                                                 .post(post)
                                                                 .author(commentDTO.getAuthor()) // 수정
                                                                 .content(commentDTO.getContent()) // 수정
                                                                 .build();

        CommentRepository.save(CommentEntity);
    }

    public Comment updateComment(CommentDto commentDto, long id) {
        PostEntity post = postRepository.findById(commentDto.getPost_id()) // 수정
                                        .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물을 찾을 수 없습니다."));

        Optional<Comment> existingComment = CommentRepository.findById(id);
        if (existingComment.isPresent()) {
            Comment CommentEntity = existingComment.get();
            CommentEntity.setContent(commentDto.getContent()); // 수정
            return CommentRepository.save(CommentEntity);
        } else {
            throw new ChangeSetPersister.NotFoundException("댓글을 찾을 수 없습니다."); // 적절한 예외 사용 확인
        }
    }

    public void deleteComment(CommentDto commentDTO, long id) {
        Optional<Comment> existingComment = CommentRepository.findById(id);
        if (existingComment.isPresent()) {
            CommentEntity CommentEntity = existingComment.get();
            CommentRepository.deleteById(CommentEntity.getId());

        } else {
            throw new ChangeSetPersister.NotFoundException("게시물을 찾을 수 없습니다.");
        }
    }

