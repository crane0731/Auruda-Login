package com.sw.AurudaLogin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    CAFE("카페"),
    ACCOMMODATION("숙소"),
    RESTAURANT("음식점"),
    SHOPPINGMALL("쇼핑몰"),
    TOURISTATTRACTION("관광지"),
    ACTIVITY("액티비티"),
    EVENT("축제/공연/행사"),
    CULTURALFACILITIES("문화시설");

    private static final Map<String, Category> NAME_MAP = new HashMap<>();

    static {
        for (Category category : values()) {
            NAME_MAP.put(category.koreanName, category);
        }
    }

    private final String koreanName;

    Category(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonValue
    public String getKoreanName() {
        return koreanName;
    }

    @JsonCreator
    public static Category fromKoreanName(String koreanName) {
        Category category = NAME_MAP.get(koreanName);
        if (category == null) {
            throw new IllegalArgumentException("No category with Korean name: " + koreanName);
        }
        return category;
    }
}