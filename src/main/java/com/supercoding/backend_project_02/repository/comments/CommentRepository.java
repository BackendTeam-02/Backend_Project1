package com.supercoding.backend_project_02.repository.comments;

import com.supercoding.backend_project_02.entity.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment>  findAllBy();

    List<Comment> findAllByPost_Id(long postId);
}