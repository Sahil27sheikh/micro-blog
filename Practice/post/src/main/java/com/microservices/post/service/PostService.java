package com.microservices.post.service;

import com.microservices.post.dto.PostDto;
import com.microservices.post.dto.PostWithCommentDto;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostDto getPostById(String id);

    PostWithCommentDto postWithComments(String id);
}
