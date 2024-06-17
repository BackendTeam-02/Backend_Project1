package com.supercoding.backend_project_02.service.posts.mapper;

import com.supercoding.backend_project_02.dto.posts.Post;
import com.supercoding.backend_project_02.dto.posts.PostBody;
import com.supercoding.backend_project_02.entity.posts.PostsEntity;
import com.supercoding.backend_project_02.entity.users.Users;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-14T12:45:47+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    @Override
    public Post postEntityToPost(PostsEntity postsEntity) {
        if ( postsEntity == null ) {
            return null;
        }

        Post post = new Post();

        post.setAuthor( postsEntityUserEmail( postsEntity ) );
        post.setId( postsEntity.getId() );
        post.setTitle( postsEntity.getTitle() );
        post.setContent( postsEntity.getContent() );
        post.setCreatedAt( postsEntity.getCreatedAt() );

        return post;
    }

    @Override
    public PostsEntity postBodyToPostEntity(PostBody postBody) {
        if ( postBody == null ) {
            return null;
        }

        PostsEntity postsEntity = new PostsEntity();

        postsEntity.setUser( postBodyToUsers( postBody ) );
        postsEntity.setTitle( postBody.getTitle() );
        postsEntity.setContent( postBody.getContent() );
        postsEntity.setAuthor( postBody.getAuthor() );

        return postsEntity;
    }

    private String postsEntityUserEmail(PostsEntity postsEntity) {
        if ( postsEntity == null ) {
            return null;
        }
        Users user = postsEntity.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    protected Users postBodyToUsers(PostBody postBody) {
        if ( postBody == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.email( postBody.getAuthor() );

        return users.build();
    }
}
