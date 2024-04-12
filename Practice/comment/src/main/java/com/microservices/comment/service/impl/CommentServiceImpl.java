package com.microservices.comment.service.impl;

import com.microservices.comment.dto.CommentDto;
import com.microservices.comment.dto.Post;
import com.microservices.comment.entity.Comment;
import com.microservices.comment.exception.ResourceNotFoundException;
import com.microservices.comment.feign_clients.PostClient;
import com.microservices.comment.repository.CommentRepository;
import com.microservices.comment.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private ModelMapper modelMapper;
    private RestTemplate restTemplate;
    private PostClient postClient;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, RestTemplate restTemplate, PostClient postClient) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.postClient = postClient;
    }

    // create new comment
    @Override
    public CommentDto createComment(String postId, CommentDto commentDto) {
        // Api Http call to Post service findPostByPostId method using Feign client.
        Post post = postClient.getPost(postId);

        if (post == null){
            throw new ResourceNotFoundException("post not found on id "+postId);
        }
        Comment comment = mapToEntity(commentDto);
        comment.setId(UUID.randomUUID().toString());
        comment.setPostId(postId);
        Comment savedComment = commentRepository.save(comment);
        return mapToDto(savedComment);
    }

    // get all comments by postId
    @Override
    public List<CommentDto> getAllCommentsByPostId(String postId) {
        // Api Http call to Post service findPostByPostId method using Feign client.
        Post post = postClient.getPost(postId);

        if (post == null){
            throw new ResourceNotFoundException("post not found on id "+postId);
        }
        List<Comment> comments = commentRepository.findByPostId(postId);
        if (comments.isEmpty()){
            throw new ResourceNotFoundException("comments not found on given post");
        }
        List<CommentDto> commentList = comments.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
        return commentList;
    }

    Comment mapToEntity(CommentDto commentDto){
        return modelMapper.map(commentDto, Comment.class);
    }
    CommentDto mapToDto(Comment comment){
        return modelMapper.map(comment, CommentDto.class);
    }
}
