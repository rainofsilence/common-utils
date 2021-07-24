package com.lindsey.coutil.sql.enums;

public enum KeyTypeEnum {
    AUTO_INCREMENT("AI", "主键自增"),
    SNOWFLAKE("SF", "雪花算法"),
    STRING("S", "String"),
    NUMBER("N", "Number"),
    ;

    private final String code;
    private final String name;

    KeyTypeEnum(String code, String name) {
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
