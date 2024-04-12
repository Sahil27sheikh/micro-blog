package com.microservices.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_1")
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 55)
    private String name;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "post_id", nullable = false)
    private String postId;

}