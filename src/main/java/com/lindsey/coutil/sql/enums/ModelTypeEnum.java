package com.lindsey.coutil.sql.enums;

/**
 * @author rainofsilence
 * @date 2022/7/30 周六
 */
public enum ModelTypeEnum {

    DEFAULT("DEFAULT", "DEFAULT"),
    ONE("ONE", "ONE"),
    ;


    private final String code;
    private final String name;

    ModelTypeEnum(String code, String name) {
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
