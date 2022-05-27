package com.github.soohea.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Instant;

@Data
public class Blog {
    private Integer id;
    @JsonIgnore
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;
    private User user;
    

}
