package com.sw.AurudaLogin.domain;

import lombok.Getter;

@Getter
public enum Grade {
    A("바람지기"),
    B("길잡이"),
    C("나그네"),
    D("떠돌이"),
    E("찾길이");
    private String commentary;

    private Grade(String commentary) {
        this.commentary = commentary;
    }

}
