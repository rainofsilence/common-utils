package com.lindsey.coutil.sql.enums;

public enum ColTypeEnum {
    STRING("S","String"),
    NUMBER("N", "Number"),
    DATETIME("DT", "Datetime"),
    ;


    private final String code;
    private final String name;

    ColTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
