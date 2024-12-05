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
    public static final String NO_FOUND_FILE = "No found file";
    public static final String FAILED_DELETE_FILE = "FAILED DELETE FILE";
    private ErrorMessage() {
    }
}
