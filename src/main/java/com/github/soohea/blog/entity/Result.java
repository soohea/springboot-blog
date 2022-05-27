package com.github.soohea.blog.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Result<T> {
    String status;
    String msg;
    T data;

    protected Result(String status, String msg) {
        this(status, msg, null);
    }

}

