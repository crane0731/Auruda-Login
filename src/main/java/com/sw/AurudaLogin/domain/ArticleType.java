package com.sw.AurudaLogin.domain;

public enum ArticleType {
    FREE_BOARD("자유게시판"),//
    TRAVEL_REVIEW("여행후기"),
    FIND_COMPANION("투게더"),
    TRAVEL_RATING("여행평가");

    private String comment;

    private ArticleType(String comment) {
        this.comment = comment;
    }

}
