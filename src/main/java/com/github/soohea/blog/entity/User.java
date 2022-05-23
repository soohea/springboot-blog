package com.github.soohea.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
public class User {
    Integer id;
    String username;
    @JsonIgnore
    String encryptedPassword;
    String avatar;
    Instant createdAt;
    Instant updatedAt;

    public User(Integer id, String username,String encryptedPassword) {
        this.id = id;
        this.username = username;
        this.encryptedPassword=encryptedPassword;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }



}
