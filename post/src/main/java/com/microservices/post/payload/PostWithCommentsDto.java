package com.microservices.post.payload;

import com.microservices.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostWithCommentsDto {
    private String id;
    private String title;
    private String description;
    private String content;
    private List<CommentDto> comments;
}
