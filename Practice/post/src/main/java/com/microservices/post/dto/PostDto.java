package com.microservices.post.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
    @NotEmpty
    @Size(min = 3, message = "title should be minimum 3 characters")
    private String title;
    @NotNull(message = "content can not be null")
    private String content;
    @NotEmpty
    @Size(min = 5, message = "description should be minimum 5 characters")
    private String description;
}
