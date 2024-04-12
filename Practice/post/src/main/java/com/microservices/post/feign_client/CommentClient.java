package com.microservices.post.feign_client;

import com.microservices.post.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentClient {
    @GetMapping("/api/comments/getComment/{id}")
    List<CommentDto> allComments(@PathVariable String id);
}
