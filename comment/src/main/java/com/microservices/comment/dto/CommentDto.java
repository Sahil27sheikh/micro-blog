package com.microservices.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String id;
    @NotEmpty
    @Size(min = 3, message = "name should be minimum 3 characters")
    private String name;
    @NotEmpty(message = "comment body not be empty")
    private String body;
    @NotEmpty(message = "email can not be empty")
    private String email;
    private String postId;
}
