package com.sw.AurudaLogin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Theme {
    //힐링,카페,엑티비티,문화,음식,쇼핑
    HEALING("힐링"),
    CAFE("카페"),
    ACTIVITY("엑티비티"),
    CULTURE("문화"),
    FOOD("음식"),
    SHOPPING("쇼핑"),
    EVENT("행사"),
    THEMEPARK("테마파크");

    private final String koreanName;

    Theme(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonValue
    public String getKoreanName() {
        return koreanName;
    }

    // 한글 이름으로 Theme Enum을 찾는 메서드
    @JsonCreator
    public static Theme fromKoreanName(String koreanName) {
        for (Theme theme : Theme.values()) {
            if (theme.koreanName.equals(koreanName)) {
                return theme;
            }
        }
        throw new IllegalArgumentException("Unknown theme: " + koreanName);
    }
}
