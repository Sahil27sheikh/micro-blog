package com.microservices.post.service.impl;

import com.microservices.post.entity.Post;
import com.microservices.post.payload.PostWithCommentsDto;
import com.microservices.post.repository.PostRepository;
import com.microservices.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Post createPost(Post post) {
        String postId = UUID.randomUUID().toString();
        post.setId(postId);
        Post save = postRepository.save(post);
        return save;
    }

    @Override
    public Post getPost(String postId) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post = byId.get();
        return post;
    }

    @Override
    public PostWithCommentsDto getPostWithComments(String postId) {
        Post post = postRepository.findById(postId).get();
        ArrayList comments = restTemplate.getForObject("http://COMMENT-SERVICE/api/comments/" + post.getId(),
                ArrayList.class);

        PostWithCommentsDto pWCD = new PostWithCommentsDto();
        pWCD.setId(post.getId());
        pWCD.setTitle(post.getTitle());
        pWCD.setContent(post.getContent());
        pWCD.setDescription(post.getDescription());
        pWCD.setComments(comments);
        return pWCD;
    }


}
