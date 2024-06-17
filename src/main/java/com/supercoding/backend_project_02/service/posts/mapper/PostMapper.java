package com.supercoding.backend_project_02.service.posts.mapper;

import com.supercoding.backend_project_02.dto.posts.Post;
import com.supercoding.backend_project_02.dto.posts.PostBody;
import com.supercoding.backend_project_02.entity.posts.PostsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "user.email", target = "author")
    Post postEntityToPost(PostsEntity postsEntity);

    @Mapping(source = "author", target = "user.email") // TODO USER
    PostsEntity postBodyToPostEntity(PostBody postBody);
}