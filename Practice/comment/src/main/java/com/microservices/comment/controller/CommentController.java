package com.microservices.comment.controller;

import com.microservices.comment.dto.CommentDto;
import com.microservices.comment.entity.Comment;
import com.microservices.comment.service.CommentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}")
    @CircuitBreaker(name = "postBreaker", fallbackMethod = "postFallback")
    public ResponseEntity<?> createComment
            (@PathVariable String postId,
             @Valid @RequestBody CommentDto commentDto,
             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto comment = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    // fallback method.
    public ResponseEntity<?> postFallback (String postId,
                                           CommentDto commentDto,
                                           BindingResult bindingResult,
                                           Exception ex
                    ){
        CommentDto comment = new CommentDto();
        comment.setId("404");
        comment.setPostId("404");
        comment.setName("Server Down");
        comment.setBody("Server Down");
        comment.setEmail("Server Down");
        return new ResponseEntity<>(comment, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/getComment/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable String postId){
        List<CommentDto> comments = commentService.getAllCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
