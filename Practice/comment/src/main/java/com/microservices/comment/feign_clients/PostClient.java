package com.microservices.comment.feign_clients;

import com.microservices.comment.dto.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "POST-SERVICE")
public interface PostClient {
    @GetMapping("/api/posts/{id}")
    Post getPost(@PathVariable String id);
}
