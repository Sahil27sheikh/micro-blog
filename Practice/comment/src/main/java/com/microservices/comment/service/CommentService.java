package com.microservices.comment.service;

import com.microservices.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(String postId, CommentDto commentDto);

    List<CommentDto> getAllCommentsByPostId(String postId);
}
