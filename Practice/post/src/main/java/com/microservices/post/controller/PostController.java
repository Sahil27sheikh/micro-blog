package com.microservices.post.controller;

import com.microservices.post.dto.PostDto;
import com.microservices.post.dto.PostWithCommentDto;
import com.microservices.post.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Create Post
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto post =  postService.createPost(postDto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    // Fetch post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String id){
        PostDto post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // get all comments along with post using postId
    @GetMapping("/{id}/comments")
    @CircuitBreaker(name = "commentBreaker", fallbackMethod = "commentFallback")
    public ResponseEntity<PostWithCommentDto> postWithComments(@PathVariable String id){
        PostWithCommentDto postWithComments = postService.postWithComments(id);
        return new ResponseEntity<>(postWithComments, HttpStatus.OK);
    }

    public ResponseEntity<PostWithCommentDto> commentFallback(String id, Exception ex){
        System.out.println("service is down please try later: "+ex.getMessage());
        ex.printStackTrace();
        PostWithCommentDto dto = new PostWithCommentDto();
        dto.setId("404");
        dto.setTitle("server down");
        dto.setContent("server down");
        dto.setDescription("Server down");
        dto.setCommentDtoList(null);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

