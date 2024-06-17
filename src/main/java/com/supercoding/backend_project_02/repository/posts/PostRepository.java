package com.supercoding.backend_project_02.repository.posts;

import com.supercoding.backend_project_02.entity.posts.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostsEntity, Integer> {

    List<PostsEntity> findByUserEmail(String email);
}