package com.gurakbu.delivery.domain.menu.entity;

public enum MenuCategory {
    MAIN,       // 메인 메뉴
    SIDE,       // 사이드 메뉴
    DRINK,      // 음료
    ALCOHOL,    // 주류
    EVENT,      // 이벤트 메뉴
    ETC;        // 기타

    // Enum -> String으로 변환 (JSON 변환 시 필요)
    @Override
    public String toString() {
        return name();
    }
}