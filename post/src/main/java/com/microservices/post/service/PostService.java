package com.microservices.post.service;

import com.microservices.post.entity.Post;
import com.microservices.post.payload.PostWithCommentsDto;

public interface PostService {
    Post createPost(Post post);

    Post getPost(String postId);

    PostWithCommentsDto getPostWithComments(String postId);
}
