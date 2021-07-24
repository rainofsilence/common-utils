package com.lindsey.coutil.sql.enums;

public enum OptTypeEnum {
    INSERT("INS", "Insert"),
    UPDATE("UPD", "Update"),
    DELETE("DEL", "Delete"),
    ;

    private final String code;
    private final String name;

    OptTypeEnum(String code, String name) {
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
