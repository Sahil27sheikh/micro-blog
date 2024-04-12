package com.microservices.post.service.impl;

import com.microservices.post.dto.CommentDto;
import com.microservices.post.dto.PostDto;
import com.microservices.post.dto.PostWithCommentDto;
import com.microservices.post.entity.Post;
import com.microservices.post.exception.ResourceNotFoundException;
import com.microservices.post.feign_client.CommentClient;
import com.microservices.post.repository.PostRepository;
import com.microservices.post.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private RestTemplate restTemplate;
    private CommentClient commentClient;
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, RestTemplate restTemplate, CommentClient commentClient) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.commentClient = commentClient;
    }

    // create new post
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        post.setId(UUID.randomUUID().toString());
        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }

    // get post using postId
    @Override
    public PostDto getPostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post not found on id " + id)
        );
        return mapToDto(post);
    }

    // get all comments along with post
    @Override
    public PostWithCommentDto postWithComments(String id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post is not found on id " + id)
        );
        // Http call to Comment service getAllCommentsByPostId() Api endpoint using Feign client.
        List<CommentDto> commentsLists = commentClient.allComments(post.getId());

        if(commentsLists == null){
            throw new ResourceNotFoundException("comments not found on given post id "+id);
        }

        PostDto postDto = mapToDto(post);

        PostWithCommentDto pWCD = new PostWithCommentDto();
        pWCD.setId(post.getId());
        pWCD.setTitle(post.getTitle());
        pWCD.setContent(post.getContent());
        pWCD.setDescription(post.getDescription());
        pWCD.setCommentDtoList(commentsLists);
        return pWCD;
    }

    Post mapToEntity(PostDto postDto){
        return modelMapper.map(postDto, Post.class);
    }

    PostDto mapToDto(Post post){
        return modelMapper.map(post, PostDto.class);
    }


}
