package com.sw.AurudaLogin.exception;

import lombok.Getter;

@Getter
public final class ErrorMessage {
    public static final String NO_USER = "No user found";
    public static final String NO_FRIEND = "No friend found";;
    public static final String NO_FRIEND_REQUEST = "No friend request found";;
    public static final String NO_ARTICLE = "No article found";
    public static final String NO_COMMENT = "No comment found";
    public static final String INVALID_SORTING_TYPE = "Invalid sorting type";
    public static final String NO_SORTING_TYPE = "No found sorting type";
    public static final String INVALID_REFRESH_TOKEN="Invalid refresh token";
    private ErrorMessage() {
    }
}
