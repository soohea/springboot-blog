package com.github.soohea.blog.utils;

public class AssertUtils {
    public static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new IllegalArgumentException(message);
        }
    }
}
