package com.microservices.post.controller;

import com.microservices.post.entity.Post;
import com.microservices.post.payload.PostWithCommentsDto;
import com.microservices.post.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    // create a new post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        Post savedPost = postService.createPost(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    // find post using postId
    @GetMapping("/{postId}") // http://localhost:8081/api/posts/2aaede23-54a9-445d-b3f8-a5d6cc6c733a
    public ResponseEntity<Post> getPostByPostId(@PathVariable String postId){
        Post post = postService.getPost(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // post with list of comments
    @GetMapping("/{postId}/comments") // http://localhost:8081/api/posts/2aaede23-54a9-445d-b3f8-a5d6cc6c733a/comments
    @CircuitBreaker(name = "commentBreaker", fallbackMethod = "commentFallback")
    // what ever name you apply to hear it has to be match with instance name which you define in circuit breaker
    // configuration.
    // 'fallbackMethod' is nothing but if the service is down then it will call the defined method automatically. and
    // this method return back the message the service is down. if the services is append running fallbackMethod is
    // not run. Note: whatever method name we define in 'fallbackMethod' exactly same method has to be present in our
    // controller and method has exactly same argument on which '@CircuitBreaker' annotation is applied and method
    // also has exactly same return type or else it will not work.
    public ResponseEntity<PostWithCommentsDto> postWithComments(@PathVariable String postId){
        PostWithCommentsDto postWithComments = postService.getPostWithComments(postId);
        return new ResponseEntity<>(postWithComments, HttpStatus.OK);
    }

    public ResponseEntity<PostWithCommentsDto> commentFallback(String postId, Exception e) { // method has same name
        // of 'fallbackMethod' defined name, same argument and same return type but throw the exception.
        System.out.println("Fallback is executed because service is down: "+ e.getMessage());

        e.printStackTrace();

        PostWithCommentsDto pWCD = new PostWithCommentsDto();
        pWCD.setId("1234"); // dummy data
        pWCD.setTitle("Service Down");
        pWCD.setContent("Service Down");
        pWCD.setDescription("Service Down");
        return new ResponseEntity<>(pWCD, HttpStatus.BAD_REQUEST);
    }
}
